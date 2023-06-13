package lib.dehaat.ledger.presentation.model.creditsummary

data class CreditSummaryViewData(
	val credit: CreditViewData,
	val overdue: OverdueViewData,
	val info: InfoViewData,
	val isOrderingBlocked: Boolean,
	val isCreditLimitExhausted: Boolean,
	val isOverdueLimitExhausted: Boolean,
	val showOverdueWidget: Boolean,
	val showOrderingBlockedWidget: Boolean,
	val ledgerOverdueAmount: Double,
	val ledgerEarliestOverdueDate: String
)
