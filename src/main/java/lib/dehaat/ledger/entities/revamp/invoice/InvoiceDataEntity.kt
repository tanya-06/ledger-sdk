package lib.dehaat.ledger.entities.revamp.invoice

import lib.dehaat.ledger.entities.detail.invoice.InterestOverdueEntity

data class InvoiceDataEntity(
    val creditNotes: List<CreditNoteEntity>,
    val productsInfo: ProductsInfoEntityV2,
    val summary: SummaryEntityV2,
    val prepaidAndCreditInfo: PrepaidAndCreditInfoEntity?,
    val interestOverdueEntity: InterestOverdueEntity?
) {
    data class PrepaidAndCreditInfoEntity(
        val prepaidAmount: String?,
        val creditAmount: String?,
    )
}
