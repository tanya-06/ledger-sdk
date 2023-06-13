package lib.dehaat.ledger.entities.detail.invoice

data class InterestOverdueEntity(
	val invoiceStatus: String?,
	val statusVariable: String?,
	val totalInvoiceAmount: Double?,
	val totalInterestCharged: Double?,
	val totalRemainingAmount: Double?,
	val interestPerDay: Double?
)