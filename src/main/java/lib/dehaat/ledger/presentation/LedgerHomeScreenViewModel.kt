package lib.dehaat.ledger.presentation

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orFalse
import com.dehaat.androidbase.helper.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.DaysToFilterUseCase
import lib.dehaat.ledger.domain.usecases.GetCreditSummaryUseCase
import lib.dehaat.ledger.domain.usecases.GetTransactionSummaryUseCase
import lib.dehaat.ledger.entities.creditsummary.CreditSummaryEntity
import lib.dehaat.ledger.entities.revamp.creditsummary.CreditSummaryEntityV2
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.annotations.ILBottomBarType
import lib.dehaat.ledger.presentation.ledger.annotations.PayNowScreenType
import lib.dehaat.ledger.presentation.ledger.state.LedgerFilterViewModelState
import lib.dehaat.ledger.presentation.ledger.state.LedgerHomeScreenViewModelState
import lib.dehaat.ledger.presentation.mapper.ViewDataMapper
import lib.dehaat.ledger.presentation.model.downloadledger.DownloadLedgerData
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.toStartAndEndDates
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar
import javax.inject.Inject

@HiltViewModel
class LedgerHomeScreenViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val getCreditSummaryUseCase: GetCreditSummaryUseCase,
	private val getTransactionSummaryUseCase: GetTransactionSummaryUseCase,
	private val daysToFilterUseCase: DaysToFilterUseCase,
	private val mapper: ViewDataMapper,
	private val analytics: LibLedgerAnalytics
) : ViewModel() {

	private val _updateLedgerStartDate = MutableSharedFlow<DownloadLedgerData>()
	val updateLedgerStartDate = _updateLedgerStartDate.asSharedFlow()

	val isFinancedDc by lazy {
		savedStateHandle.get<Boolean>(LedgerConstants.KEY_IS_FINANCED).orFalse()
	}

	val partnerId by lazy {
		savedStateHandle.get<String>(LedgerConstants.KEY_PARTNER_ID).orEmpty()
	}
	val dcName by lazy {
		savedStateHandle.get<String>(LedgerConstants.KEY_DC_NAME).orEmpty()
	}
	val isLMSActivated: Boolean
		get() = viewModelState.value.isLMSActivated

	private val _selectedDaysToFilterEvent = MutableSharedFlow<DaysToFilter>()
	val selectedDaysToFilterEvent: SharedFlow<DaysToFilter> get() = _selectedDaysToFilterEvent

	private val _uiEvent = MutableSharedFlow<UiEvent>()
	val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

	private val viewModelState = MutableStateFlow(LedgerHomeScreenViewModelState())
	val uiState = viewModelState
		.map { it.toUiState() }
		.stateIn(
			viewModelScope,
			SharingStarted.Eagerly,
			viewModelState.value.toUiState()
		)

	private val _filterViewModelState = MutableStateFlow(LedgerFilterViewModelState())
	val filterUiState = _filterViewModelState
		.map { it.toUiState() }
		.stateIn(
			viewModelScope,
			SharingStarted.Eagerly,
			_filterViewModelState.value.toUiState()
		)

	init {
		getInitialData()
	}

	fun getInitialData() = viewModelScope.launch {
		callingAPI()
		if (isFinancedDc) {
			val creditSummary = async { getCreditSummaryForFinancedDC() }
			val transactionSummary = async { getTransactionForFinancedDC() }
			processFinancedDCInitialData(creditSummary.await(), transactionSummary.await())
		} else {
			val creditSummary = async { getCreditSummaryForNonFinancedDC() }
			val transactionSummary = async { getTransactionForFinancedDC() }
			processNonFinancedDCInitialData(creditSummary.await(), transactionSummary.await())
		}
		calledAPI()
	}

	private fun processFinancedDCInitialData(
		creditSummary: APIResultEntity<CreditSummaryEntityV2?>,
		transactionSummary: APIResultEntity<TransactionSummaryEntity?>
	) {
		processCreditSummaryFinancedResponse(creditSummary)
		processTransactionForFinancedDC(transactionSummary)
	}

	private fun processNonFinancedDCInitialData(
		creditSummary: APIResultEntity<CreditSummaryEntity?>,
		transactionSummary: APIResultEntity<TransactionSummaryEntity?>
	) {
		processCreditSummaryNonFinancedResponse(creditSummary)
		processTransactionForFinancedDC(transactionSummary)
	}

	private suspend fun getCreditSummaryForFinancedDC() =
		getCreditSummaryUseCase.getCreditSummaryV2(partnerId)

	private suspend fun getCreditSummaryForNonFinancedDC() =
		getCreditSummaryUseCase.getCreditSummary(partnerId)

	private fun processCreditSummaryFinancedResponse(
		response: APIResultEntity<CreditSummaryEntityV2?>
	) = response.processAPIResponseWithFailureSnackBar(
		onFailure = { errorMessage ->
			sendShowSnackBarEvent(errorMessage)
			viewModelState.update {
				it.copy(errorMessage = errorMessage, isError = true)
			}
		},
		handleSuccess = { creditSummary ->
			viewModelState.update {
				it.copy(
					widgetsViewData = mapper.toWidgetsViewData(creditSummary),
					outstandingAmount = creditSummary.totalOutstandingAmount,
					isLMSActivated = creditSummary.externalFinancierSupported,
					isError = false,
					errorMessage = null
				)
			}
			callInViewModelScope {
				_updateLedgerStartDate.emit(
					DownloadLedgerData(
						creditSummary.firstLedgerEntryDate.orZero(),
						creditSummary.ledgerEndDate
					)
				)
			}
		}
	)

	private fun processCreditSummaryNonFinancedResponse(
		response: APIResultEntity<CreditSummaryEntity?>
	) = response.processAPIResponseWithFailureSnackBar(
		onFailure = { errorMessage ->
			sendShowSnackBarEvent(errorMessage)
			viewModelState.update {
				it.copy(errorMessage = errorMessage, isError = true)
			}
		},
		handleSuccess = { creditSummary ->
			viewModelState.update {
				it.copy(
					widgetsViewData = mapper.toWidgetsViewData(creditSummary),
					outstandingAmount = creditSummary.credit.totalOutstandingAmount,
					isLMSActivated = creditSummary.credit.externalFinancierSupported,
					isError = false,
					errorMessage = null
				)
			}
			callInViewModelScope {
				_updateLedgerStartDate.emit(
					DownloadLedgerData(
						creditSummary.info.firstLedgerEntryDate.orZero(),
						creditSummary.info.ledgerEndDate
					)
				)
			}
		}
	)

	private suspend fun getTransactionForFinancedDC(
		daysToFilter: DaysToFilter? = null
	): APIResultEntity<TransactionSummaryEntity?> = with(daysToFilter?.toStartAndEndDates()) {
		getTransactionSummaryUseCase.getTransactionSummaryV2(
			partnerId,
			this?.first,
			this?.second
		)
	}

	private fun processTransactionForFinancedDC(response: APIResultEntity<TransactionSummaryEntity?>) {
		if (response is APIResultEntity.Success && response.data != null) {
			viewModelState.update {
				it.copy(
					ledgerTotalCalculation = mapper.toLedgerTotalCalculation(response.data),
					outstandingCalculationUiState = mapper.toOutstandingCalculationViewData(response.data!!),
					holdAmountViewData = mapper.toHoldAmountViewData(response.data)
				)
			}
		}
		calledAPI()
	}

	fun updateFilter(daysToFilter: DaysToFilter) = callInViewModelScope {
		_selectedDaysToFilterEvent.emit(updateDateOrder(daysToFilter))
		_filterViewModelState.update {
			it.copy(appliedFilter = daysToFilter)
		}
	}

	private fun updateDateOrder(daysToFilter: DaysToFilter): DaysToFilter {
		if (daysToFilter is DaysToFilter.CustomDays && daysToFilter.fromDateMilliSec > daysToFilter.toDateMilliSec) {
			return with(daysToFilter) {
				DaysToFilter.CustomDays(
					fromDateMilliSec = toDateMilliSec,
					toDateMilliSec = fromDateMilliSec
				)
			}
		}
		return daysToFilter
	}

	fun hideFilterBottomSheet() = _filterViewModelState.update {
		it.copy(showFilterSheet = false)
	}

	fun showFilterBottomSheet() = _filterViewModelState.update {
		it.copy(showFilterSheet = true)
	}

	private fun calledAPI() = updateProgressDialog(false)

	private fun callingAPI() = updateProgressDialog(true)

	fun updateProgressDialog(show: Boolean) = viewModelState.update {
		it.copy(isLoading = show)
	}

	private fun sendShowSnackBarEvent(message: String) = callInViewModelScope {
		_uiEvent.emit(UiEvent.ShowSnackBar(message))
	}

	fun getSelectedDates(
		daysToFilter: DaysToFilter
	) = daysToFilterUseCase.getSelectedDates(
		daysToFilter
	)

	fun showWalletFTUEBottomSheet() = viewModelState.update {
		it.copy(showFirstTimeFTUEDialog = true)
	}

	fun hideWalletFTUEBottomSheet() = viewModelState.update {
		it.copy(showFirstTimeFTUEDialog = false)
	}

	fun dismissWalletFTUEBottomSheet() = viewModelState.update {
		it.copy(dismissFirstTimeFTUEDialog = true)
	}

	fun onWidgetClicked(bundle: Bundle) {
		val amount = bundle.getDouble(LedgerConstants.AMOUNT)
		val date = bundle.getString(LedgerConstants.DATE)
		when (bundle.getString(LedgerConstants.BOTTOM_BAR_TYPE)) {
			ILBottomBarType.ORDERING_BLOCKED -> {
				analytics.onOverdueClicked(false, false, amount, date)
			}

			ILBottomBarType.ORDERING_WILL_BLOCKED -> {
				analytics.onOverdueClicked(true, false, amount, date)
			}

			ILBottomBarType.INTEREST_WILL_START -> {
				analytics.onInterestClicked(false, false, amount, date, isFinancedDc)
			}

			ILBottomBarType.INTEREST_STARTED -> {
				analytics.onInterestClicked(true, false, amount, date, isFinancedDc)
			}
		}
	}

	fun onPayNowClicked() {
		analytics.onPayNowClicked(PayNowScreenType.LEDGER)
	}

}
