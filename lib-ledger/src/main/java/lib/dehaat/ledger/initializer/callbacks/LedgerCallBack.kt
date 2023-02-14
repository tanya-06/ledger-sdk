package lib.dehaat.ledger.initializer.callbacks

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData

typealias PayNowClick = (creditSummaryViewData: CreditSummaryViewData?) -> Unit
typealias DownloadInvoiceIntent = (Context, String) -> PendingIntent?
typealias DownloadInvoiceSuccess = (invoiceDownloadData: InvoiceDownloadData) -> Unit
typealias ExceptionHandler = (ex: Exception) -> Unit
typealias FirebaseScreenLogger = (Context, String) -> Unit

typealias PaymentOptionsClick = (
    resultLauncher: ActivityResultLauncher<Intent?>
) -> Unit

data class LedgerCallBack(
    val onClickPayNow: PayNowClick,
    val onRevampPayNowClick: PaymentOptionsClick,
    val onDownloadInvoiceSuccess: DownloadInvoiceSuccess,
    val onPaymentOptionsClick: PaymentOptionsClick,
    val downloadInvoiceIntent: DownloadInvoiceIntent,
    val exceptionHandler: ExceptionHandler,
    val firebaseScreenLogger: FirebaseScreenLogger
)
