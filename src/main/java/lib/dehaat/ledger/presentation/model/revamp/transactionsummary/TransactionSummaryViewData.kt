package lib.dehaat.ledger.presentation.model.revamp.transactionsummary

data class TransactionSummaryViewData(
    val purchaseAmount: String,
    val paymentAmount: String,
    val interestAmount: String,
    val holdAmountViewData: HoldAmountViewData,
    val debitHoldAmount: String,
    val releaseAmount: String,
)

data class ABSViewData(
    val amount: Double,
    val lastMoveScheme: String?,
    val showBanner: Boolean,
    val lastMovedSchemeAmount: String?
)

data class HoldABSViewData(
	val formattedAbsHoldBalance: String,
	val formattedLastMovedSchemeAmount: String,
	val showBanner: Boolean,
	val absHoldBalance: Double?,
)

data class HoldAmountViewData(
	val formattedTotalHoldBalance: String,
	val absViewData: HoldABSViewData,
	val formattedPrepaidHoldAmount: String,
	val prepaidHoldAmount: Double?,
)