package lib.dehaat.ledger.initializer.callbacks

import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.InvoiceDetailDataViewData

interface LedgerCallbacks {
    fun onClickPayNow(creditSummaryViewData: CreditSummaryViewData?)
    fun onClickDownloadInvoice(invoiceDetailDataViewData: InvoiceDetailDataViewData?)
    fun onPaymentOptionsClick(creditSummaryViewData: CreditSummaryViewData?)
}
