package lib.dehaat.ledger.entities.revamp.invoice

data class SummaryEntityV2(
	val interestBeingCharged: Boolean?,
	val interestDays: Int?,
	val interestStartDate: Long?,
	val invoiceAmount: String?,
	val invoiceDate: Long,
	val invoiceId: String,
	val processingFee: String?,
	val totalOutstandingAmount: String?,
	val totalInterestCharged: Double?,
	val totalInterestPaid: Double?,
	val totalInterestOutstanding: Double?,
	val penaltyAmount: String?,
	val invoiceAge: Long?,
	val isInterestSubVented: Boolean?
)
