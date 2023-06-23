package lib.dehaat.ledger.presentation.ledger.state

import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.util.getEndYearRange

data class LedgerTransactionsViewModelState(
	val downloadLedgerState: DownloadLedgerState = DownloadLedgerState(
		false, DownloadLedgerFilter.YEARLY, getEndYearRange(), null, null
	),
	val snackbarType: SnackBarType? = null
) {
	fun toUiState() = LedgerTransactionsUIState(downloadLedgerState, snackbarType)
}