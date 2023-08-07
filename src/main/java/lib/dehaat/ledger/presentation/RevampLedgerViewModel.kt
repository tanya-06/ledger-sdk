package lib.dehaat.ledger.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orZero
import com.dehaat.wallet.domain.usecase.GetWalletTotalAmountUseCase
import com.dehaat.wallet.presentation.extensions.getApiFailureError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import lib.dehaat.ledger.domain.usecases.GetCreditSummaryUseCase
import lib.dehaat.ledger.domain.usecases.GetTransactionSummaryUseCase
import lib.dehaat.ledger.domain.usecases.LedgerDownloadUseCase
import lib.dehaat.ledger.entities.revamp.creditsummary.CreditSummaryEntityV2
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFormat
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.SelectDateType
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.LedgerViewModelState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.TransactionViewModelState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit.AvailableCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.outstandingcreditlimit.OutstandingCreditLimitViewState
import lib.dehaat.ledger.presentation.mapper.ViewDataMapper
import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import lib.dehaat.ledger.presentation.model.downloadledger.DownloadLedgerData
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.toStartAndEndDates
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.util.getEndYearRange
import lib.dehaat.ledger.util.getFailureError
import lib.dehaat.ledger.util.getMonthYear
import lib.dehaat.ledger.util.getTimeFromMonthYear
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar

@HiltViewModel
class RevampLedgerViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val getCreditSummaryUseCase: GetCreditSummaryUseCase,
	private val getTransactionSummaryUseCase: GetTransactionSummaryUseCase,
	private val ledgerDownloadUseCase: LedgerDownloadUseCase,
	private val getWalletTotalAmount: GetWalletTotalAmountUseCase,
    val ledgerAnalytics: LibLedgerAnalytics,
    private val mapper: ViewDataMapper
) : BaseViewModel() {

	var downloadFormat: String = DownloadLedgerFormat.PDF
	val partnerId by lazy { savedStateHandle.get<String>(LedgerConstants.KEY_PARTNER_ID) ?: "" }
	val dcName by lazy { savedStateHandle.get<String>(LedgerConstants.KEY_DC_NAME) ?: "" }

	private val _updateLedgerStartDate = MutableSharedFlow<DownloadLedgerData>()
	val updateLedgerStartDate = _updateLedgerStartDate.asSharedFlow()

	private val _selectedDaysToFilterEvent = MutableSharedFlow<DaysToFilter>()
	val selectedDaysToFilterEvent: SharedFlow<DaysToFilter> get() = _selectedDaysToFilterEvent

	private val viewModelState = MutableStateFlow(LedgerViewModelState())
	private val transactionsViewModelState = MutableStateFlow(TransactionViewModelState())

	val uiState = viewModelState.map { it.toUIState() }.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUIState()
	)

	val transactionUIState = transactionsViewModelState.map { it.toUIState() }.stateIn(
		viewModelScope, SharingStarted.Eagerly, transactionsViewModelState.value.toUIState()
	)

	var availableCreditLimitViewState: AvailableCreditLimitViewState? = null
		private set

	var outstandingCreditLimitViewState: OutstandingCreditLimitViewState? = null
		private set

	private val _downloadStarted = MutableSharedFlow<Boolean>()
	val downloadStarted: Flow<Boolean> = _downloadStarted

	private val _uiEvent = MutableSharedFlow<UiEvent>()
	val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    init {
        getCreditSummaryFromServer()
        getTransactionSummaryFromServer()
        getTotalWalletAmountFromServer()
    }

	fun showFilterBottomSheet() = viewModelState.update {
		it.copy(showFilterSheet = true)
	}

	fun hideFilterBottomSheet() = viewModelState.update {
		it.copy(showFilterSheet = false)
	}

	fun showWalletFTUEBottomSheet() = viewModelState.update {
        it.copy(showFirstTimeFTUEDialog = true)
    }

    fun dismissWalletFTUEBottomSheet() = viewModelState.update {
        it.copy(dismissFirstTimeFTUEDialog = true)
    }

    fun hideWalletFTUEBottomSheet() = viewModelState.update {
        it.copy(showFirstTimeFTUEDialog = false)
    }
    fun getTransactionSummaryFromServer(daysToFilter: DaysToFilter? = null) = callInViewModelScope {
        callingAPI()
        val dates = daysToFilter?.toStartAndEndDates()
        val response = getTransactionSummaryUseCase.getTransactionSummaryV2(
            partnerId, dates?.first, dates?.second
        )
        calledAPI()
        processTransactionSummaryResponse(response)
    }

	private fun processTransactionSummaryResponse(
		result: APIResultEntity<TransactionSummaryEntity?>
	) = result.processAPIResponseWithFailureSnackBar(::sendFailureEvent) { entity ->
		val transactionSummaryViewData = mapper.toTransactionSummaryViewData(entity)
		val outstandingCalculationUiState = mapper.toOutstandingCalculationViewData(entity)
		transactionsViewModelState.update { ledgerDetailViewModelState ->
			ledgerDetailViewModelState.copy(
				isLoading = false,
				summary = transactionSummaryViewData,
				outstandingCalculationUiState = outstandingCalculationUiState
			)
		}
	}

    private fun getTotalWalletAmountFromServer() = callInViewModelScope {
        callingAPI()
        when (val response = getWalletTotalAmount()) {
            is APIResultEntity.Success -> {
                viewModelState.update {
                    it.copy(walletBalance = response.data?.totalWalletBalance.toString().getAmountInRupees())
                }
            }
            is APIResultEntity.Failure -> {
                sendFailureEvent(response.getApiFailureError())
            }
        }
        callingAPI()
    }

	private fun getCreditSummaryFromServer() {
		callInViewModelScope {
			callingAPI()
			val response = getCreditSummaryUseCase.getCreditSummaryV2(partnerId)
			calledAPI()
			processCreditSummaryResponse(response)
		}
	}

	private fun processCreditSummaryResponse(
		result: APIResultEntity<CreditSummaryEntityV2?>
	) = result.processAPIResponseWithFailureSnackBar(::sendFailureEvent) { creditSummaryEntity ->
		val creditSummaryViewData = mapper.toCreditSummaryViewData(creditSummaryEntity)
		callInViewModelScope {
			_updateLedgerStartDate.emit(
				DownloadLedgerData(
					creditSummaryViewData.firstLedgerEntryDate.orZero(),
					creditSummaryViewData.ledgerEndDate
				)
			)
		}
		transactionsViewModelState.update {
			it.copy(
				downloadLedgerState = it.downloadLedgerState.copy(
					selectedDateRange = getEndYearRange(
						creditSummaryViewData.ledgerEndDate
					)
				)
			)
		}
		availableCreditLimitViewState = mapper.toAvailableCreditLimitViewState(creditSummaryEntity)
		outstandingCreditLimitViewState =
			mapper.toOutstandingCreditLimitViewState(creditSummaryEntity)
		viewModelState.update { state ->
			state.copy(
				summaryViewData = creditSummaryViewData, isSuccess = true
			)
		}
	}

	private fun sendFailureEvent(message: String) {
		resetSnackBarType()
		viewModelState.update {
			it.copy(
				isError = true, errorMessage = message
			)
		}
	}

	private fun calledAPI() = updateProgressDialog(false)

	private fun callingAPI() = updateProgressDialog(true)

	fun updateProgressDialog(show: Boolean) = viewModelState.update {
		it.copy(isLoading = show)
	}

	private fun updateDLProgressDialog(show: Boolean) = transactionsViewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(isLoading = show))
	}

	fun updateFilter(daysToFilter: DaysToFilter) = callInViewModelScope {
		_selectedDaysToFilterEvent.emit(daysToFilter)
		viewModelState.update {
			it.copy(selectedFilter = daysToFilter)
		}
	}

	fun getSelectedDates(daysToFilter: DaysToFilter) = when (daysToFilter) {
		DaysToFilter.SevenDays -> calculateTime(7)
		DaysToFilter.OneMonth -> calculateTime(30)
		DaysToFilter.ThreeMonth -> calculateTime(90)
		else -> null
	}

	private fun calculateTime(dayCount: Int): Pair<Long, Long> {
		val daysSec = dayCount * 24 * 60 * 60
		val currentDaySec = System.currentTimeMillis() / 1000
		val pastDaySec = currentDaySec.minus(daysSec)
		return Pair(pastDaySec, currentDaySec)
	}

	fun downloadLedger() = callInViewModelScope {
		pushLedgerDownloadEvent()
		updateDLProgressDialog(true)
		val result =
			ledgerDownloadUseCase.invoke(partnerId, getFromDate(), getToDate(), downloadFormat) {
				callInViewModelScope { _downloadStarted.emit(true) }
			}
		processDownloadLedgerResult(result)
		updateDLProgressDialog(false)
	}

	private fun pushLedgerDownloadEvent() {
		ledgerAnalytics.onDownloadClicked(downloadFormat, getTimeFrameMode(), getTimeFrame())
	}

	private fun getTimeFrame() =
		"${
			getMonthYear(getFromDate().orZero().times(1000))
		} - ${getMonthYear(getToDate().orZero().times(1000))}"

	private fun getTimeFrameMode() = with(transactionsViewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == DownloadLedgerFilter.YEARLY) LedgerConstants.FINANCIAL_YEAR else LedgerConstants.CUSTOM
	}

	private fun processDownloadLedgerResult(result: APIResultEntity<String?>) {
		if (result is APIResultEntity.Failure) {
			resetSnackBarType()
			callInViewModelScope { _uiEvent.emit(UiEvent.ShowSnackbar(result.getFailureError())) }
		}
	}

	private fun resetSnackBarType() {
		viewModelState.update { it.copy(snackbarType = null) }
	}

	private fun getFromDate(): Long? = with(transactionsViewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == DownloadLedgerFilter.YEARLY) {
			selectedDateRange.startDate
		} else {
			fromDate?.run { getTimeFromMonthYear(this, true) }
		}
	}

	private fun getToDate(): Long? = with(transactionsViewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == DownloadLedgerFilter.YEARLY) {
			if (downloadFormat == DownloadLedgerFormat.PDF) selectedDateRange.pdfEndDate else selectedDateRange.excelEndDate
		} else {
			toDate?.run { getTimeFromMonthYear(this, false) }
		}
	}

	fun outstandingPaymentValid() = viewModelState.update {
		val summaryViewData = it.summaryViewData?.copy(
			isOrderingBlocked = false, creditLineStatus = null
		)
		it.copy(summaryViewData = summaryViewData)
	}

	fun onMonthYearSelected(monthYear: Pair<Int, Int>, selectedDateType: String) {
		when (selectedDateType) {
			SelectDateType.FROM -> transactionsViewModelState.update {
				it.copy(downloadLedgerState = it.downloadLedgerState.copy(fromDate = monthYear))
			}

			SelectDateType.TO -> transactionsViewModelState.update {
				it.copy(downloadLedgerState = it.downloadLedgerState.copy(toDate = monthYear))
			}
		}
	}

	fun onDateRangeSelected(dateRange: DateRange) = transactionsViewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(selectedDateRange = dateRange))
	}

	fun updateSelectedFilter(selectedFilter: Int) = transactionsViewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(selectedDownloadFilter = selectedFilter))
	}

	fun updateSnackBarType(snackbarType: SnackBarType) = viewModelState.update {
		it.copy(snackbarType = snackbarType)
	}
}
