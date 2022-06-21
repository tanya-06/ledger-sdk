package lib.dehaat.ledger.presentation.ledger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import lib.dehaat.ledger.initializer.LedgerParentApp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.navigation.LedgerNavigation
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.resources.LedgerTheme

@AndroidEntryPoint
class LedgerDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!LedgerSDK.isCurrentAppAvailable()) {
            Toast.makeText(
                this,
                "Please initialize Ledger SDK with current app.",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        val partnerId = intent.getStringExtra(LedgerConstants.KEY_PARTNER_ID)
        val dcName = intent.getStringExtra(LedgerConstants.KEY_DC_NAME)
        partnerId?.let {
            setContent {
                LedgerTheme {
                    LedgerNavigation(
                        dcName = dcName ?: "Ledger",
                        partnerId = partnerId,
                        ledgerColors = LedgerSDK.currentApp.ledgerColors,
                        finishActivity = { finish() },
                        ledgerCallbacks = (LedgerSDK.currentApp as? LedgerParentApp.DBA)?.ledgerCallBack
                    )
                }
            }
        } ?: run {
            Toast.makeText(
                this,
                "Partner Id is missing, Please provide partner id.",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }
    }
}