package lib.dehaat.ledger.initializer.callbacks

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadStatus

interface LedgerCallbacks {
    fun onClickPayNow(creditSummaryViewData: CreditSummaryViewData?)
    fun onClickDownloadInvoice(invoiceDownloadStatus: InvoiceDownloadStatus)
    fun onPaymentOptionsClick(
        creditSummaryViewData: CreditSummaryViewData?,
        resultLauncher: ActivityResultLauncher<Intent?>
    )
}
