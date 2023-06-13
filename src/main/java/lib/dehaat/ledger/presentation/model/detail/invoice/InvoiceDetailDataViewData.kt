package lib.dehaat.ledger.presentation.model.detail.invoice

import lib.dehaat.ledger.presentation.model.revamp.invoice.InterestOverdueViewData

data class InvoiceDetailDataViewData(
    val summary: SummaryViewData,
    val loans: List<LoanViewData>?,
    val overdueInfo: OverdueInfoViewData,
    val productsInfo: ProductsInfoViewData,
    val interestOverdueViewData: InterestOverdueViewData?
)
