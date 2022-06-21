package lib.dehaat.ledger.entities.detail.creditnote

data class SummaryEntity(
    val amount: String,
    val invoiceNumber: String?,
    val timestamp: Long,
    val reason: String
)