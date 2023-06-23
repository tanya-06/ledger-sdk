package lib.dehaat.ledger.presentation.ledger.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orZero
import com.dehaat.wallet.domain.usecase.GetWalletTotalAmountUseCase
import com.dehaat.wallet.presentation.extensions.getAmtInRupees
import com.dehaat.wallet.presentation.extensions.getApiFailureError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetTransactionsUseCase
import lib.dehaat.ledger.domain.usecases.LedgerDownloadUseCase
import lib.dehaat.ledger.entities.transactions.TransactionEntity
import lib.dehaat.ledger.framework.network.BasePagingSourceWithResponse
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_PARTNER_ID
import lib.dehaat.ledger.presentation.LibLedgerAnalytics
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFormat
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.SelectDateType
import lib.dehaat.ledger.presentation.ledger.transactions.state.TransactionsViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.util.getEndYearRange
import lib.dehaat.ledger.util.getFailureError
import lib.dehaat.ledger.util.getMonthYear
import lib.dehaat.ledger.util.getTimeFromMonthYear

@HiltViewModel
class LedgerTransactionViewModel @Inject constructor(
	private val getTransactionsUseCase: GetTransactionsUseCase,
	private val ledgerDownloadUseCase: LedgerDownloadUseCase,
	private val getWalletTotalAmount: GetWalletTotalAmountUseCase,
	private val mapper: LedgerViewDataMapper,
	val ledgerAnalytics: LibLedgerAnalytics,
	savedStateHandle: SavedStateHandle
) : BaseViewModel() {

	var downloadFormat: String = DownloadLedgerFormat.PDF

	private val partnerId by lazy {
		savedStateHandle.get<String>(KEY_PARTNER_ID) ?: throw Exception(
			"Partner id should not null"
		)
	}

	private val _uiEvent = MutableSharedFlow<UiEvent>()
	val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

	private val viewModelState = MutableStateFlow(TransactionsViewModelState())
	val uiState = viewModelState
		.map { it.toUiState() }
		.stateIn(
			viewModelScope,
			SharingStarted.Eagerly,
			viewModelState.value.toUiState()
		)

	private val _downloadStarted = MutableSharedFlow<Boolean>()
	val downloadStarted: Flow<Boolean> = _downloadStarted

	var transactionsList: Flow<PagingData<TransactionViewData>> private set

	init {
		transactionsList = getTransactionPaging()
		getTotalWalletAmountFromServer()
	}

	fun applyOnlyPenaltyInvoicesFilter(onlyPenaltyInvoices: Boolean) {
		viewModelState.update {
			it.copy(onlyPenaltyInvoices = onlyPenaltyInvoices)
		}
		refresh()
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

	fun applyDaysFilter(dayFilter: DaysToFilter) {
		viewModelState.update {
			it.copy(daysToFilter = dayFilter)
		}
		getTransactionPaging()
		refresh()
	}

	private fun getTransactionPaging() = Pager(
		config = PagingConfig(pageSize = 1, enablePlaceholders = true),
		pagingSourceFactory = { getPagingSource() }
	).flow.cachedIn(viewModelScope)

	private fun getPagingSource() =
		object :
			BasePagingSourceWithResponse<TransactionViewData, List<TransactionEntity>>(
				{ pageNumber: Int, pageSize: Int ->
					val response = getTransactionsFromServer(pageSize, pageNumber)
					processTransactionListResponse(response)
				},
				onResponse = { _, _ -> },
				parseDataList = {
					mapper.toTransactionsDataEntity(it)
				}
			) {}

	private suspend fun getTransactionsFromServer(
		pageSize: Int,
		pageNumber: Int
	): APIResultEntity<List<TransactionEntity>> {
		val (fromDate, toDate) = getFromAndToDate()
		return getTransactionsUseCase.getTransactions(
			partnerId = partnerId,
			fromDate = fromDate,
			toDate = toDate,
			onlyPenaltyInvoices = viewModelState.value.onlyPenaltyInvoices,
			limit = pageSize,
			offset = (pageNumber - 1) * pageSize
		)
	}

	private fun getFromAndToDate(): Pair<Long?, Long?> {
		return when (val daysToFilter = viewModelState.value.daysToFilter) {
			DaysToFilter.All -> Pair(null, null)
			DaysToFilter.SevenDays -> calculateTimeInMillisecond(7)
			DaysToFilter.OneMonth -> calculateTimeInMillisecond(31)
			DaysToFilter.ThreeMonth -> calculateTimeInMillisecond(31 * 3)
			is DaysToFilter.CustomDays -> calculateCustomDaysMillisecond(daysToFilter)
		}
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

	private fun processTransactionListResponse(
		response: APIResultEntity<List<TransactionEntity>>
	) = when (response) {
		is APIResultEntity.Success -> {
			response.data
		}

		is APIResultEntity.Failure -> {
			sendShowSnackBarEvent((response.getFailureError()))
			emptyList()
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

	private fun refresh() {
		callInViewModelScope { _uiEvent.emit(UiEvent.RefreshList) }
	}

	fun downloadLedger() = callInViewModelScope {
		pushLedgerDownloadEvent()
		alterDLProgress(true)
		val result =
			ledgerDownloadUseCase.invoke(partnerId, getFromDate(), getToDate(), downloadFormat) {
				callInViewModelScope { _downloadStarted.emit(true) }
			}
		alterDLProgress(false)
		processDownloadLedgerResult(result)
	}

	private fun pushLedgerDownloadEvent() {
		ledgerAnalytics.onDownloadClicked(downloadFormat, getTimeFrameMode(), getTimeFrame())
	}

	private fun getTimeFrame() =
		"${
			getMonthYear(getFromDate().orZero().times(1000))
		} - ${getMonthYear(getToDate().orZero().times(1000))}"

	private fun getTimeFrameMode() = with(viewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == DownloadLedgerFilter.YEARLY) lib.dehaat.ledger.presentation.LedgerConstants.FINANCIAL_YEAR else lib.dehaat.ledger.presentation.LedgerConstants.CUSTOM
	}

	private fun alterDLProgress(showLoader: Boolean) = viewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(isLoading = showLoader))
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
		if (selectedDownloadFilter == DownloadLedgerFilter.YEARLY) {
			selectedDateRange.startDate
		} else {
			fromDate?.run { getTimeFromMonthYear(this, true) }
		}
	}

	private fun getToDate(): Long? = with(viewModelState.value.downloadLedgerState) {
		if (selectedDownloadFilter == DownloadLedgerFilter.YEARLY) {
			selectedDateRange.pdfEndDate
		} else {
			toDate?.run { getTimeFromMonthYear(this, false) }
		}
	}

	fun updateDLSelectedFilter(selectedFilter: Int) = viewModelState.update {
		it.copy(downloadLedgerState = it.downloadLedgerState.copy(selectedDownloadFilter = selectedFilter))
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

	fun updateSnackBarType(snackbarType: SnackBarType) = viewModelState.update {
		it.copy(snackbarType = snackbarType)
	}

	fun updateEndDate(endTime: Long?) = endTime?.let {
		viewModelState.update {
			it.copy(
				downloadLedgerState = it.downloadLedgerState.copy(
					selectedDateRange = getEndYearRange(endTime)
				)
			)
		}
	}
}
