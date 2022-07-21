package lib.dehaat.ledger.initializer.callbacks

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData

interface LedgerCallbacks {
    fun onClickPayNow(creditSummaryViewData: CreditSummaryViewData?)
    fun onClickDownloadInvoice(invoiceDownloadData: InvoiceDownloadData)
    fun onPaymentOptionsClick(
        creditSummaryViewData: CreditSummaryViewData?,
        resultLauncher: ActivityResultLauncher<Intent?>
    )
}
