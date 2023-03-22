package lib.dehaat.ledger.presentation.model.detail.creditnote

data class SummaryViewData(
    val amount: String,
    val invoiceNumber: String?,
    val timestamp: Long,
    val reason: String
)