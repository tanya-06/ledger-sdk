package lib.dehaat.ledger.presentation.ledger.revamp.state.credits

import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation.OutstandingCalculationUiState
import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.TransactionSummaryViewData
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.util.getEndYearRange

data class LedgerViewModelState(
	val summaryViewData: SummaryViewData? = null,
	val showFilterSheet: Boolean = false,
	val selectedFilter: DaysToFilter = DaysToFilter.All,
	val isError: Boolean = false,
	val errorMessage: String = "",
	val isLoading: Boolean = false,
	val isSuccess: Boolean = false,
	val snackbarType: SnackBarType? = null,
    val walletBalance: Double = 0.0
) {
	fun toUIState() = LedgerUIState(
		summaryViewData = summaryViewData,
		showFilterSheet = showFilterSheet,
		appliedFilter = selectedFilter,
		state = when {
			isSuccess -> UIState.SUCCESS
			isError -> UIState.ERROR(errorMessage)
			isLoading -> UIState.LOADING
			else -> UIState.SUCCESS
		},
		snackbarType = snackbarType,
        walletBalance = walletBalance
    )
}

data class LedgerUIState(
	val summaryViewData: SummaryViewData?,
	val showFilterSheet: Boolean,
	val appliedFilter: DaysToFilter,
	val state: UIState,
	val snackbarType: SnackBarType?,
    val walletBalance: Double
)

data class TransactionViewModelState(
	val summary: TransactionSummaryViewData? = null,
	val outstandingCalculationUiState: OutstandingCalculationUiState? = null,
	val isError: Boolean = false,
	val errorMessage: String = "",
	val isLoading: Boolean = false,
	val isSuccess: Boolean = false,
	val downloadLedgerState: DownloadLedgerState = DownloadLedgerState(
		false, DownloadLedgerFilter.YEARLY, getEndYearRange(), null, null, true
	)
) {
	fun toUIState() = TransactionUIState(
		summaryViewData = summary,
		outstandingCalculationUiState = outstandingCalculationUiState,
		state = when {
			isSuccess -> UIState.SUCCESS
			isError -> UIState.ERROR(errorMessage)
			isLoading -> UIState.LOADING
			else -> UIState.SUCCESS
		},
		downloadLedgerState = downloadLedgerState.copy(
			enableDownloadBtn = shouldEnableDownloadBtn(downloadLedgerState)
		)
	)

	private fun shouldEnableDownloadBtn(downloadLedgerState: DownloadLedgerState): Boolean =
		with(downloadLedgerState) {
			if ((selectedDownloadFilter == DownloadLedgerFilter.CUSTOM) && (fromDate == null || toDate == null)) {
				return@with false
			}
			return@with !isLoading
		}
}

data class TransactionUIState(
	val summaryViewData: TransactionSummaryViewData?,
	val outstandingCalculationUiState: OutstandingCalculationUiState?,
	val state: UIState,
	val downloadLedgerState: DownloadLedgerState
)

data class DownloadLedgerState(
	val isLoading: Boolean,
	val selectedDownloadFilter: Int,
	val selectedDateRange: DateRange,
	val fromDate: Pair<Int, Int>?,
	val toDate: Pair<Int, Int>?,
	val enableDownloadBtn: Boolean
)
