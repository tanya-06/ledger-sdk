package lib.dehaat.ledger.entities.creditsummary

data class OverdueEntity(
    val totalOverdueLimit: String,
    val totalOverdueAmount: String,
    val minPaymentAmount: String?,
    val minPaymentDueDate: Long?
)
