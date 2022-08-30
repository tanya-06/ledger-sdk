package lib.dehaat.ledger.presentation.ledger.transactions.state

import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter

data class TransactionsViewModelState(
    val isLoading: Boolean = false,
    val onlyPenaltyInvoices: Boolean = false,
    val daysToFilter: DaysToFilter = DaysToFilter.All,
) {
    fun toUiState() = LedgerDetailUIState(
        isLoading = isLoading,
        onlyPenaltyInvoices = onlyPenaltyInvoices
    )
}

data class LedgerDetailUIState(
    val isLoading: Boolean,
    val onlyPenaltyInvoices: Boolean,
)