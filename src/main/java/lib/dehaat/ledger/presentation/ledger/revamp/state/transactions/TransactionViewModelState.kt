package lib.dehaat.ledger.presentation.ledger.revamp.state.transactions

data class TransactionViewModelState(
	val showWeeklyInterestDecreasingLabel: Boolean = false
) {
	fun toUIState() = TransactionUiState(
		showWeeklyInterestDecreasingLabel = showWeeklyInterestDecreasingLabel
	)
}

data class TransactionUiState(
	val showWeeklyInterestDecreasingLabel: Boolean
)
