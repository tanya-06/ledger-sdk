package lib.dehaat.ledger.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orFalse
import com.dehaat.androidbase.helper.orZero
import com.dehaat.wallet.domain.usecase.GetWalletTotalAmountUseCase
import com.dehaat.wallet.presentation.extensions.getAmtInRupees
import com.dehaat.wallet.presentation.extensions.getApiFailureError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.DaysToFilterUseCase
import lib.dehaat.ledger.domain.usecases.GetTransactionsUseCase
import lib.dehaat.ledger.domain.usecases.LedgerDownloadUseCase
import lib.dehaat.ledger.entities.revamp.transaction.TransactionEntityV2
import lib.dehaat.ledger.entities.transactions.TransactionEntity
import lib.dehaat.ledger.framework.network.BasePagingSourceWithResponse
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFormat
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.SelectDateType
import lib.dehaat.ledger.presentation.ledger.state.LedgerFilterViewModelState
import lib.dehaat.ledger.presentation.ledger.state.LedgerTransactions
import lib.dehaat.ledger.presentation.ledger.state.LedgerTransactionsViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.util.getEndYearRange
import lib.dehaat.ledger.util.getFailureError
import lib.dehaat.ledger.util.getMonthYear
import lib.dehaat.ledger.util.getTimeFromMonthYear
import lib.dehaat.ledger.util.toMonthYearName
import javax.inject.Inject

@HiltViewModel
class LedgerTransactionsViewModel @Inject constructor(
	val ledgerAnalytics: LibLedgerAnalytics,
	private val savedStateHandle: SavedStateHandle,
	private val getTransactionsUseCase: GetTransactionsUseCase,
	private val daysToFilterUseCase: DaysToFilterUseCase,
	private val ledgerDownloadUseCase: LedgerDownloadUseCase,
	private val getWalletTotalAmount: GetWalletTotalAmountUseCase,
	private val mapper: LedgerViewDataMapper
) : ViewModel() {

	var downloadFormat: String = DownloadLedgerFormat.PDF

	private val _downloadStarted = MutableSharedFlow<Boolean>()
	val downloadStarted: Flow<Boolean> = _downloadStarted

	private val _uiEvent = MutableSharedFlow<UiEvent>()
	val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

	private val partnerId by lazy {
		savedStateHandle.get<String>(LedgerConstants.KEY_PARTNER_ID).orEmpty()
	}

	var transactionsList: Flow<PagingData<LedgerTransactions>>
		private set

	val isFinancedDc by lazy {
		savedStateHandle.get<Boolean>(LedgerConstants.KEY_IS_FINANCED).orFalse()
	}

	private val viewModelState = MutableStateFlow(LedgerTransactionsViewModelState())
	val uiState = viewModelState.map { it.toUiState() }.stateIn(
			viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
		)

	private val _filterViewModelState = MutableStateFlow(LedgerFilterViewModelState())
	val filterUiState = _filterViewModelState.map { it.toUiState() }.stateIn(
			viewModelScope, SharingStarted.Eagerly, _filterViewModelState.value.toUiState()
		)

	init {
		transactionsList = getTransactionPaging()
		getTotalWalletAmountFromServer()
	}

	private fun getTotalWalletAmountFromServer() = callInViewModelScope {
		if (LedgerSDK.isWalletActive) {
			when (val response = getWalletTotalAmount()) {
				is APIResultEntity.Success -> {
					viewModelState.update {
						it.copy(
							walletBalance = response.data?.totalWalletBalance.toString()
								.getAmtInRupees()
						)
					}
				}
				is APIResultEntity.Failure -> {
					sendShowSnackBarEvent(response.getApiFailureError())
				}
			}
		}
	}

	private fun sendShowSnackBarEvent(message: String) {
		resetSnackBarType()
		viewModelState.update {
			it.copy(isLoading = false)
		}
		viewModelScope.launch {
			_uiEvent.emit(UiEvent.ShowSnackBar(message))
		}
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

	private fun updateDLProgressDialog(show: Boolean) = viewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(isLoading = show))
	}

	private fun pushLedgerDownloadEvent() {
		ledgerAnalytics.onDownloadClicked(downloadFormat, getTimeFrameMode(), getTimeFrame())
	}

	private fun getTimeFrame() = "${
		getMonthYear(getFromDate().orZero().times(1000))
	} - ${getMonthYear(getToDate().orZero().times(1000))}"

	private fun getTimeFrameMode() = with(viewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter.YEARLY) lib.dehaat.ledger.presentation.LedgerConstants.FINANCIAL_YEAR else lib.dehaat.ledger.presentation.LedgerConstants.CUSTOM
	}

	private fun processDownloadLedgerResult(result: APIResultEntity<String?>) {
		if (result is APIResultEntity.Failure) {
			resetSnackBarType()
			callInViewModelScope { _uiEvent.emit(UiEvent.ShowSnackBar(result.getFailureError())) }
		}
	}

	private fun resetSnackBarType() {
		viewModelState.update { it.copy(snackbarType = null) }
	}

	private fun getFromDate(): Long? = with(viewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter.YEARLY) {
			selectedDateRange.startDate
		} else {
			fromDate?.run { getTimeFromMonthYear(this, true) }
		}
	}

	private fun getToDate(): Long? = with(viewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter.YEARLY) {
			if (downloadFormat == lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFormat.PDF) selectedDateRange.pdfEndDate else selectedDateRange.excelEndDate
		} else {
			toDate?.run { getTimeFromMonthYear(this, false) }
		}
	}

	fun onMonthYearSelected(monthYear: Pair<Int, Int>, selectedDateType: String) {
		when (selectedDateType) {
			SelectDateType.FROM -> viewModelState.update {
				it.copy(downloadLedgerState = it.downloadLedgerState.copy(fromDate = monthYear))
			}

			SelectDateType.TO -> viewModelState.update {
				it.copy(downloadLedgerState = it.downloadLedgerState.copy(toDate = monthYear))
			}
		}
	}

	fun onDateRangeSelected(dateRange: DateRange) = viewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(selectedDateRange = dateRange))
	}

	fun updateSelectedFilter(selectedFilter: Int) = viewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(selectedDownloadFilter = selectedFilter))
	}

	fun updateSnackBarType(snackbarType: SnackBarType) = viewModelState.update {
		it.copy(snackbarType = snackbarType)
	}

	private fun getTransactionPaging() =
		Pager(config = PagingConfig(pageSize = 1, enablePlaceholders = true),
			pagingSourceFactory = { getPagingSource() }).flow.map {
			it.insertSeparators { before, after ->
				insertSeparators(before?.date.toMonthYearName(), after?.date.toMonthYearName())
			}
		}.cachedIn(viewModelScope)

	private fun insertSeparators(
		before: String, after: String
	) = if (before != after) {
		LedgerTransactions.MonthSeparator.monthlySeparator(after)
	} else {
		null
	}

	private fun getPagingSource() = object :
		BasePagingSourceWithResponse<LedgerTransactions, List<TransactionEntityV2>>({ pageNumber: Int, pageSize: Int ->
			getTransactionsFromServer(pageSize, pageNumber)
		}, onResponse = { _, _ -> }, parseDataList = { transactionEntity ->
			if (_filterViewModelState.value.showWeeklyInterestDecreasingLabel.not()) {
				_filterViewModelState.update {
					it.copy(
						showWeeklyInterestDecreasingLabel = mapper.getWeeklyInterestLabel(
							transactionEntity
						)
					)
				}
			}
			mapper.toLedgerTransactions(transactionEntity)
		}) {}

	private suspend fun getTransactionsFromServer(
		pageSize: Int, pageNumber: Int
	): List<TransactionEntityV2> {
		val (fromDate, toDate) = getFromAndToDate()
		return if (isFinancedDc) {
			val response = getTransactionsUseCase.getTransactionsV2(
				partnerId = partnerId,
				fromDate = fromDate,
				toDate = toDate,
				limit = pageSize,
				offset = (pageNumber - 1) * pageSize
			)
			processFinancedDCTransaction(response)
		} else {
			val response = getTransactionsUseCase.getTransactions(
				partnerId = partnerId,
				fromDate = fromDate,
				toDate = toDate,
				limit = pageSize,
				offset = (pageNumber - 1) * pageSize
			)
			processNonFinancedDCTransaction(response)
		}
	}

	private fun processFinancedDCTransaction(
		response: APIResultEntity<List<TransactionEntityV2>>
	) = when (response) {
		is APIResultEntity.Success -> {
			response.data
		}

		is APIResultEntity.Failure -> {
			emptyList()
		}
	}

	private fun processNonFinancedDCTransaction(
		response: APIResultEntity<List<TransactionEntity>>
	) = when (response) {
		is APIResultEntity.Success -> {
			response.data.map { it.toTransactionEntityV2() }
		}

		is APIResultEntity.Failure -> {
			emptyList()
		}
	}

	private fun TransactionEntity.toTransactionEntityV2() = TransactionEntityV2(
		amount = amount,
		creditNoteReason = creditNoteReason,
		date = date,
		erpId = erpId,
		interestStartDate = interestStartDate,
		interestEndDate = interestEndDate,
		ledgerId = ledgerId,
		locusId = locusId?.toIntOrNull(),
		partnerId = partnerId,
		paymentMode = paymentMode,
		source = source,
		sourceNo = sourceNo,
		type = type,
		unrealizedPayment = unrealizedPayment,
		isInterestSubVented = false,
		fromDate = fromDate,
		toDate = toDate,
		adjustmentAmount = adjustmentAmount,
		schemeName = schemeName,
		creditAmount = creditAmount,
		prepaidAmount = prepaidAmount,
		invoiceStatus = invoiceStatus,
		statusVariable = statusVariable,
		totalInvoiceAmount = totalInvoiceAmount,
		totalInterestCharged = totalInterestCharged,
		totalRemainingAmount = totalRemainingAmount
	)

	private fun getFromAndToDate() = when (val filter = _filterViewModelState.value.appliedFilter) {
		DaysToFilter.All -> Pair(null, null)
		DaysToFilter.SevenDays -> calculateTimeInMillisecond(7)
		DaysToFilter.OneMonth -> calculateTimeInMillisecond(31)
		DaysToFilter.ThreeMonth -> calculateTimeInMillisecond(31 * 3)
		is DaysToFilter.CustomDays -> calculateCustomDaysMillisecond(filter)
	}

	private fun calculateCustomDaysMillisecond(dayCount: DaysToFilter.CustomDays): Pair<Long, Long> {
		val currentDaySec = dayCount.toDateMilliSec / 1000
		val pastDaySec = dayCount.fromDateMilliSec / 1000
		return Pair(pastDaySec, currentDaySec)
	}

	private fun calculateTimeInMillisecond(dayCount: Int): Pair<Long, Long> {
		val daysSec = dayCount * 24 * 60 * 60
		val currentDaySec = System.currentTimeMillis() / 1000
		val pastDaySec = currentDaySec.minus(daysSec)
		return Pair(pastDaySec, currentDaySec)
	}

	fun applyDaysFilter(daysFilter: DaysToFilter) {
		_filterViewModelState.update {
			it.copy(appliedFilter = daysFilter)
		}
		transactionsList = getTransactionPaging()
	}

	fun getSelectedDates(
		daysToFilter: DaysToFilter
	) = daysToFilterUseCase.getSelectedDates(daysToFilter)

	fun updateEndDate(time: Long?) = viewModelState.update {
		it.copy(
			downloadLedgerState = it.downloadLedgerState.copy(
				selectedDateRange = getEndYearRange(time)
			)
		)
	}
}
