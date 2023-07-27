package lib.dehaat.ledger.presentation.ledger.transactions.state

import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.util.getEndYearRange

data class TransactionsViewModelState(
	val isLoading: Boolean = false,
	val onlyPenaltyInvoices: Boolean = false,
	val daysToFilter: DaysToFilter = DaysToFilter.All,
	val downloadLedgerState: DownloadLedgerState = DownloadLedgerState(
		false, DownloadLedgerFilter.YEARLY, getEndYearRange(), null, null
	),
	val snackbarType:SnackBarType? = null
) {
	fun toUiState() = LedgerDetailUIState(
		isLoading = isLoading,
		onlyPenaltyInvoices = onlyPenaltyInvoices,
		downloadLedgerState = downloadLedgerState,
		snackbarType = snackbarType
	)
}

data class LedgerDetailUIState(
	val isLoading: Boolean,
	val onlyPenaltyInvoices: Boolean,
	val downloadLedgerState: DownloadLedgerState,
	val snackbarType: SnackBarType?,
)