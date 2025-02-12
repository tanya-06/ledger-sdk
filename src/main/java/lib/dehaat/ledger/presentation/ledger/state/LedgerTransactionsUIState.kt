package lib.dehaat.ledger.presentation.ledger.state

import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType

data class LedgerTransactionsUIState(
	val isLoading: Boolean,
	val downloadLedgerState: DownloadLedgerState,
	val snackbarType: SnackBarType?,
	val walletBalance: String
)