package lib.dehaat.ledger.presentation.ledger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.toArgb
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.amazonaws.mobile.client.AWSMobileClient
import com.dehaat.androidbase.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.navigation.LedgerNavigation
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerConstants.FLOW_TYPE
import lib.dehaat.ledger.presentation.LedgerConstants.FLOW_TYPE_DATA
import lib.dehaat.ledger.presentation.common.NavDoubleType
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.NotificationHandler
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LedgerDetailActivity : ComponentActivity() {

	private lateinit var args: Args

	@Inject
	lateinit var notificationHandler: NotificationHandler

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		args = getArgs(intent)

		args.language?.let {
			setLedgerLanguage(it)
		}

		if (!LedgerSDK.isCurrentAppAvailable()) {
			showToast(getString(R.string.initialise_ledger))
			finish()
			return
		}

		if (args.partnerId.isEmpty() && LedgerSDK.isDebug) {
			showToast("PartnerId missing (check dehaat-center-API)")
			finish()
			return
		}

		modifyStatusBar(LedgerSDK.currentApp.ledgerColors)

		AWSMobileClient.getInstance().initialize(this).execute()

		setContent {
			LedgerTheme {
				LedgerNavigation(
					dcName = args.dcName,
					partnerId = args.partnerId,
					isDCFinanced = args.isDCFinanced,
					ledgerColors = LedgerSDK.currentApp.ledgerColors,
					finishActivity = { finish() },
					ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack,
                    flowType = args.flowType,
                    flowTypeData = getFlowTypeData(args.flowTypeData ?: bundleOf()),
					openOrderDetailFragment = LedgerSDK.openOrderDetailFragment,
					onDownloadClick = {
						val ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack
						notificationHandler.notificationBuilder.setSmallIcon(LedgerSDK.appIcon)
						if (it.isFailed) {
							showToast(getString(R.string.tech_problem))
						} else {
							notificationHandler.apply {
								if (it.progressData.bytesCurrent == 100) {
									notificationBuilder.apply {
										setContentText(getString(R.string.invoice_download_success))
										setContentIntent(
											ledgerCallbacks.downloadInvoiceIntent.invoke(
												this@LedgerDetailActivity,
												it.filePath
											)
										)
									}
									ledgerCallbacks.onDownloadInvoiceSuccess(it)
									showToast(getString(R.string.invoice_download_success))
								} else {
									notificationBuilder.setContentText(getString(R.string.invoice_download_in_progress))
								}
								notificationBuilder.setProgress(
									it.progressData.bytesTotal,
									it.progressData.bytesCurrent,
									false
								)
								notifyBuilder()
							}
						}
					},
					getWalletFTUEStatus = LedgerSDK.getWalletFTUEStatus,
					setWalletFTUEStatus = LedgerSDK.setWalletFTUEStatus,
					getWalletHelpVideoId = LedgerSDK.getWalletHelpVideoId
				)
			}
		}
	}

	private fun getFlowTypeData(bundle: Bundle) = bundle.keySet().map {
		if (it == LedgerConstants.AMOUNT) {
			navArgument(it) {
				type = NavDoubleType
				defaultValue = bundle.getDouble(it)
			}
		} else {
			navArgument(it) {
				type = NavType.StringType
				defaultValue = bundle.getString(it)
				nullable = true
			}
		}
	}

	private fun setLedgerLanguage(appLanguage: String) {
		val locale = Locale(appLanguage)
		val res = this.resources
		val dm = res.displayMetrics
		val conf = res.configuration
		conf.locale = locale
		res.updateConfiguration(conf, dm)
		Locale.setDefault(locale)
	}

	private fun modifyStatusBar(ledgerColors: LedgerColors) {
		if (LedgerSDK.isDBA) {
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
			window.statusBarColor = ledgerColors.ActionBarColor.toArgb()
			WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
				true
		}
	}

	companion object {
		private const val KEY_DC_FINANCED = "KEY_DC_FINANCED"
		private const val KEY_APP_LANGUAGE = "KEY_APP_LANGUAGE"

		fun getArgs(intent: Intent) = Args(
			partnerId = intent.getStringExtra(LedgerConstants.KEY_PARTNER_ID) ?: "",
			dcName = intent.getStringExtra(LedgerConstants.KEY_DC_NAME) ?: "",
			isDCFinanced = intent.getBooleanExtra(KEY_DC_FINANCED, false),
			language = intent.getStringExtra(KEY_APP_LANGUAGE),
			flowType = intent.getStringExtra(FLOW_TYPE),
			flowTypeData = intent.getBundleExtra(FLOW_TYPE_DATA)
		)

		data class Args(
			val partnerId: String,
			val dcName: String,
			val isDCFinanced: Boolean,
			val language: String?,
			val flowType: String?,
			val flowTypeData: Bundle?
		) {
			fun build(context: Context) = Intent(
				context,
				LedgerDetailActivity::class.java
			).apply {
				putExtra(LedgerConstants.KEY_PARTNER_ID, partnerId)
				putExtra(LedgerConstants.KEY_DC_NAME, dcName)
				putExtra(KEY_DC_FINANCED, isDCFinanced)
				putExtra(KEY_APP_LANGUAGE, language)
				putExtra(FLOW_TYPE, flowType)
				putExtra(FLOW_TYPE_DATA, flowTypeData)
            }
        }
    }
}
