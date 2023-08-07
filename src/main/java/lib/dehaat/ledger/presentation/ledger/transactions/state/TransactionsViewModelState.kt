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
		false, DownloadLedgerFilter.YEARLY, getEndYearRange(), null, null, false
	),
	val snackbarType: SnackBarType? = null,
	val walletBalance: String = "0.0"
) {
	fun toUiState() = TransactionsLedgerDetailUIState(
		isLoading = isLoading,
		onlyPenaltyInvoices = onlyPenaltyInvoices,
		downloadLedgerState = downloadLedgerState.copy(
			enableDownloadBtn = shouldEnableDownloadBtn(downloadLedgerState)
		),
		snackbarType = snackbarType,
        walletBalance = walletBalance
    )

	private fun shouldEnableDownloadBtn(downloadLedgerState: DownloadLedgerState): Boolean =
		with(downloadLedgerState) {
			if ((selectedDownloadFilter == DownloadLedgerFilter.CUSTOM) && (fromDate == null || toDate == null)) {
				return@with false
			}
			return@with !isLoading
		}
}

data class TransactionsLedgerDetailUIState(
	val isLoading: Boolean,
	val onlyPenaltyInvoices: Boolean,
	val downloadLedgerState: DownloadLedgerState,
	val snackbarType: SnackBarType?,
    val walletBalance: String
)