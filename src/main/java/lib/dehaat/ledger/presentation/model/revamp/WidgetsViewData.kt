package lib.dehaat.ledger.presentation.model.revamp

data class WidgetsViewData(
    val creditLineStatus: String?,
    val creditLineSubStatus: String,
    val agedOutstandingAmount: String,
    val repaymentUnblockAmount: String,
    val ageingBannerPriority: String?,
	val showOverdueWidget: Boolean,
	val showOrderingBlockedWidget: Boolean,
	val showInterestWidget: Boolean,
	val ledgerOverdueAmount: Double,
	val ledgerEarliestOverdueDate: String,
	val ledgerInterestAmount: Double,
	val ledgerEarliestInterestDate: String,
	val showInterestNotStartedWidget: Boolean
)