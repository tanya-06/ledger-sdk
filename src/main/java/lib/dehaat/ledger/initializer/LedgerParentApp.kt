package lib.dehaat.ledger.initializer

import lib.dehaat.ledger.initializer.callbacks.DownloadInvoiceIntent
import lib.dehaat.ledger.initializer.callbacks.DownloadInvoiceSuccess
import lib.dehaat.ledger.initializer.callbacks.ExceptionHandler
import lib.dehaat.ledger.initializer.callbacks.LedgerCallBack
import lib.dehaat.ledger.initializer.callbacks.FirebaseScreenLogger
import lib.dehaat.ledger.resources.themes.AIMSColors
import lib.dehaat.ledger.resources.themes.DBAColors
import lib.dehaat.ledger.resources.themes.LedgerColors

sealed class LedgerParentApp(
    val ledgerCallBack: LedgerCallBack,
    val ledgerColors: LedgerColors
) {
    class AIMS(
        downloadInvoiceClick: DownloadInvoiceSuccess,
        downloadInvoiceIntent: DownloadInvoiceIntent,
        exceptionHandler: ExceptionHandler,
        firebaseScreenLogger: FirebaseScreenLogger,
        ledgerColors: LedgerColors = AIMSColors()
    ) : LedgerParentApp(
        LedgerCallBack(
            onNonFinancedDCPayNowClick = {},
            onFinancedDCPayNowClick = {},
            onDownloadInvoiceSuccess = downloadInvoiceClick,
            onPaymentOptionsClick = {},
            downloadInvoiceIntent = downloadInvoiceIntent,
            exceptionHandler = exceptionHandler,
            firebaseScreenLogger = firebaseScreenLogger
        ),
        ledgerColors
    )

    class DBA(
        ledgerCallBack: LedgerCallBack,
        ledgerColors: LedgerColors = DBAColors()
    ) : LedgerParentApp(
        ledgerCallBack,
        ledgerColors
    )
}
