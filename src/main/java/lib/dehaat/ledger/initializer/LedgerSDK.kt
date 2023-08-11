package lib.dehaat.ledger.initializer

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.annotation.DrawableRes
import com.dehaat.androidbase.components.SingleEventLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import lib.dehaat.ledger.framework.model.outstanding.OutstandingData
import lib.dehaat.ledger.presentation.ledger.LedgerDetailActivity
import lib.dehaat.ledger.presentation.model.downloadledger.DownloadStatusData
import java.io.File
import java.math.BigDecimal

object LedgerSDK {

	internal val outstandingDataFlow = MutableStateFlow(OutstandingData(false))
	internal val downloadStatusLiveData = SingleEventLiveData<DownloadStatusData>()
	internal lateinit var currentApp: LedgerParentApp
	internal lateinit var bucket: String
	internal var locale: String = "en"
	internal var appIcon: Int = 0
		private set

	var payNowClickScreenType: String? = null
	internal var showOutstandingTooltip = false
	internal var showLedgerDownloadCta = false
	internal var isWalletActive = false
	internal var openOrderDetailFragment: (String) -> Unit = { _ -> }
	internal var getWalletFTUEStatus: (String) -> Boolean = { _ -> false }
	internal var setWalletFTUEStatus: (String) -> Unit = { }
	internal var getWalletHelpVideoId: () -> String = { "" }

	fun init(
		context: Context,
		app: LedgerParentApp,
		bucket: String,
		@DrawableRes appIcon: Int,
		debugMode: Boolean
	) {
		currentApp = app
		this.bucket = bucket
		this.appIcon = appIcon
		this.isDebug = debugMode
	}

	fun isCurrentAppAvailable() = ::currentApp.isInitialized && ::bucket.isInitialized

	@Throws(Exception::class)
	fun openLedger(
		context: Context,
		partnerId: String,
		dcName: String,
		isDCFinanced: Boolean,
		language: String? = null,
		showOutstandingTooltip: Boolean,
		showLedgerDownload: Boolean,
		isWalletActive: Boolean,
		openOrderDetailFragment: (String) -> Unit,
		getWalletFTUEStatus: (String) -> Boolean,
		setWalletFTUEStatus: (String) -> Unit,
		getWalletHelpVideoId: () -> String,
		flowType: String? = null,
		flowTypeData: Bundle? = null
	) = if (isCurrentAppAvailable()) {
		this.showLedgerDownloadCta = showLedgerDownload
		this.openOrderDetailFragment = openOrderDetailFragment
		this.isWalletActive = isWalletActive
		this.getWalletFTUEStatus = getWalletFTUEStatus
		this.setWalletFTUEStatus = setWalletFTUEStatus
		this.getWalletHelpVideoId = getWalletHelpVideoId
		LedgerDetailActivity.Companion.Args(
			partnerId = partnerId,
			dcName = dcName,
			isDCFinanced = isDCFinanced,
			language = language,
			flowType = flowType,
			flowTypeData = flowTypeData
		).also {
			language?.let { lang -> locale = lang }
			context.startActivity(it.build(context))
			this.showOutstandingTooltip = showOutstandingTooltip
		}
	} else {
		throw Exception("Ledger not initialised Exception")
	}

	suspend fun getFile(context: Context): File? = try {
		withContext(Dispatchers.IO) {
			File(
				context.getExternalFilesDir(
					Environment.DIRECTORY_DOWNLOADS
				),
				"DeHaat"
			).apply { mkdir() }
		}
	} catch (e: Exception) {
		if (::currentApp.isInitialized) {
			currentApp.ledgerCallBack.exceptionHandler(e)
		}
		null
	}

	suspend fun updateOutStanding(
		showDialog: Boolean,
		amount: BigDecimal?,
		numberOfDays: Int?
	) {
		outstandingDataFlow.emit(
			OutstandingData(
				showDialog,
				amount,
				numberOfDays?.toString() ?: "90"
			)
		)
	}

	val isDBA: Boolean
		get() = currentApp is LedgerParentApp.DBA

	val isAIMS: Boolean
		get() = currentApp is LedgerParentApp.AIMS

	var isDebug: Boolean = false
		private set
}
