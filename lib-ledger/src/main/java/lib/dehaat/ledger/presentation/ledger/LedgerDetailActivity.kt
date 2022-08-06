package lib.dehaat.ledger.presentation.ledger

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.amazonaws.mobile.client.AWSMobileClient
import com.dehaat.androidbase.helper.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.navigation.LedgerNavigation
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.util.NotificationHandler

@AndroidEntryPoint
class LedgerDetailActivity : ComponentActivity() {

    val viewModel: LedgerDetailViewModel by viewModels()

    private lateinit var args: Args

    @Inject
    lateinit var notificationHandler: NotificationHandler

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it?.resultCode == Activity.RESULT_OK) {
            viewModel.getLedgerData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = getArgs(intent)

        args.language?.let {
            setLedgerLanguage(it)
        }

        if (!LedgerSDK.isCurrentAppAvailable()) {
            showToast("Please initialize Ledger SDK with current app.")
            finish()
            return
        }

        AWSMobileClient.getInstance().initialize(this).execute()

        setContent {
            LedgerTheme {
                LedgerNavigation(
                    dcName = args.dcName,
                    partnerId = args.partnerId,
                    ledgerColors = LedgerSDK.currentApp.ledgerColors,
                    resultLauncher = resultLauncher,
                    finishActivity = { finish() },
                    viewModel = viewModel,
                    ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack,
                    onDownloadClick = {
                        val ledgerCallbacks = LedgerSDK.currentApp.ledgerCallBack
                        notificationHandler.notificationBuilder.setSmallIcon(LedgerSDK.appIcon)
                        if (it.isFailed) {
                            showToast("Technical problem occurred, try again after some time")
                        } else {
                            notificationHandler.apply {
                                if (it.progressData.bytesCurrent == 100) {
                                    notificationBuilder.apply {
                                        setContentText("Invoice downloaded successfully")
                                        setContentIntent(
                                            ledgerCallbacks.downloadInvoiceIntent.invoke(
                                                this@LedgerDetailActivity,
                                                it.filePath
                                            )
                                        )
                                    }
                                    ledgerCallbacks.onDownloadInvoiceSuccess(it)
                                    showToast("Invoice downloaded successfully")
                                } else {
                                    notificationBuilder.setContentText("Invoice download in progress")
                                }
                                notificationBuilder.setProgress(
                                    it.progressData.bytesTotal,
                                    it.progressData.bytesCurrent,
                                    false
                                )
                                notifyBuilder()
                            }
                        }
                    }
                )
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

    companion object {

        fun getArgs(intent: Intent) = Args(
            partnerId = intent.getStringExtra(LedgerConstants.KEY_PARTNER_ID) ?: "",
            dcName = intent.getStringExtra(LedgerConstants.KEY_DC_NAME) ?: "",
            language = intent.getStringExtra(LedgerConstants.KEY_APP_LANGUAGE)
        )

        data class Args(
            val partnerId: String,
            val dcName: String,
            val language: String?
        ) {
            fun build(context: Context) = Intent(
                context,
                LedgerDetailActivity::class.java
            ).apply {
                putExtra(LedgerConstants.KEY_PARTNER_ID, partnerId)
                putExtra(LedgerConstants.KEY_DC_NAME, dcName)
                putExtra(LedgerConstants.KEY_APP_LANGUAGE, language)
            }
        }
    }
}
