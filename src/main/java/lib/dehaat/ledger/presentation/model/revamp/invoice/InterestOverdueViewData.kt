package lib.dehaat.ledger.presentation.model.revamp.invoice

data class InterestOverdueViewData(
	val invoiceStatus: String?,
	val statusVariable: String?,
	val totalInvoiceAmount: Double?,
	val totalInterestCharged: Double?,
	val totalRemainingAmount: Double?,
	val interestPerDay: Double?
)