package lib.dehaat.ledger.presentation.ledger.state

import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.util.getEndYearRange

data class LedgerTransactionsViewModelState(
	val isLoading: Boolean = false,
	val downloadLedgerState: DownloadLedgerState = DownloadLedgerState(
		false, DownloadLedgerFilter.YEARLY, getEndYearRange(), null, null, true
	),
	val snackbarType: SnackBarType? = null,
	val walletBalance: String = "0.0"
) {
	fun toUiState() = LedgerTransactionsUIState(
		isLoading,
		downloadLedgerState = downloadLedgerState.copy(
			enableDownloadBtn = shouldEnableDownloadBtn(downloadLedgerState)
		), snackbarType, walletBalance
	)

	private fun shouldEnableDownloadBtn(downloadLedgerState: DownloadLedgerState): Boolean =
		with(downloadLedgerState) {
			if ((selectedDownloadFilter == DownloadLedgerFilter.CUSTOM) && (fromDate == null || toDate == null)) {
				return@with false
			}
			return@with !isLoading
		}
}