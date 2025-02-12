package lib.dehaat.ledger.entities.revamp.creditsummary

data class CreditSummaryEntityV2(
	val bufferLimit: String,
	val creditNoteAmountTillDate: String,
	val externalFinancierSupported: Boolean,
	val interestTillDate: String,
	val minInterestAmountDue: String,
	val minInterestOutstandingDate: Long,
	val minOutstandingAmountDue: String,
	val paymentAmountTillDate: String,
	val permanentCreditLimit: String,
	val purchaseAmountTillDate: String,
	val totalAvailableCreditLimit: String,
	val totalCreditLimit: String,
	val totalOutstandingAmount: String,
	val totalPurchaseAmount: String,
	val undeliveredInvoiceAmount: String,
	val totalInterestOutstanding: String,
	val totalInterestPaid: String,
	val minimumRepaymentAmount: String?,
	val repaymentDate: Long?,
	val overdueAmount: String?,
	val overdueCreditLimit: String?,
	val creditLineStatus: String?,
	val creditLineSubStatus: String,
	val agedOutstandingAmount: Double?,
	val repaymentUnblockAmount: Double?,
	val repaymentUnblockDays: Long?,
	val holdAmount: String?,
	val ageingBannerPriority: String?,
	val penaltyInterest: Float?,
	val agedOverdueAmount: Double?,
	val firstLedgerEntryDate: Long?,
	val ledgerEndDate: Long?,
	val ledgerOverdueAmount: Double?,
	val ledgerEarliestOverdueDate: Double?,
	val ledgerInterestAmount: Double?,
	val ledgerEarliestInterestDate: Double?,
	val overdueStatus: String?,
	val interestStatus: String?
)
