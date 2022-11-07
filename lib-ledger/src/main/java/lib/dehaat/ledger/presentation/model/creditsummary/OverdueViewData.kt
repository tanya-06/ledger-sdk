package lib.dehaat.ledger.presentation.model.creditsummary

data class OverdueViewData(
    val totalOverdueLimit: String,
    val totalOverdueAmount: String?,
    val minPaymentAmount: String?,
    val minPaymentDueDate: Long?
)
