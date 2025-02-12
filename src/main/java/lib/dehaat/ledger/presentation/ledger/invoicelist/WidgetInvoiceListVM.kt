package lib.dehaat.ledger.presentation.ledger.invoicelist

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orFalse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetWidgetInvoiceListUseCase
import lib.dehaat.ledger.entities.invoicelist.InvoiceEntity
import lib.dehaat.ledger.entities.invoicelist.WidgetInvoiceListEntity
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerConstants.FLOW_TYPE
import lib.dehaat.ledger.presentation.LibLedgerAnalytics
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceListFlowType
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceStatus
import lib.dehaat.ledger.presentation.ledger.annotations.LedgerStatus
import lib.dehaat.ledger.presentation.ledger.annotations.PayNowScreenType
import lib.dehaat.ledger.presentation.ledger.invoicelist.state.BottomBarData
import lib.dehaat.ledger.presentation.ledger.invoicelist.state.WidgetInvoiceListVMState
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar
import javax.inject.Inject

@HiltViewModel
class WidgetInvoiceListVM @Inject constructor(
	private val getWidgetInvoiceListUseCase: GetWidgetInvoiceListUseCase,
	private val mapper: LedgerViewDataMapper,
	private val analytics: LibLedgerAnalytics,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val partnerId by lazy {
		savedStateHandle.get<String>(LedgerConstants.KEY_PARTNER_ID) ?: throw Exception(
			"Partner id should not null"
		)
	}

	val isFinancedDc by lazy {
		savedStateHandle.get<Boolean>(LedgerConstants.KEY_IS_FINANCED).orFalse()
	}

	private val widgetType = savedStateHandle.get<String>(FLOW_TYPE).orEmpty()
	private val amount = savedStateHandle.get<Double>(LedgerConstants.AMOUNT).orZero()
	private val date = savedStateHandle.get<String>(LedgerConstants.DATE).orEmpty()
	private val bottomBarType =
		savedStateHandle.get<String>(LedgerConstants.BOTTOM_BAR_TYPE).orEmpty()

	private val _uiEvent = MutableSharedFlow<UiEvent>()
	val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

	private val viewModelState = MutableStateFlow(WidgetInvoiceListVMState())
	val uiState = viewModelState.map { it.toUiState() }.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState()
	)

	init {
		updateWidgetType()
		getInvoicesList()
	}

	private fun updateWidgetType() = viewModelState.update {
		it.copy(widgetType = widgetType)
	}

	private fun getInvoicesList() = callInViewModelScope {
		alterLoader(true)
		val invoiceLisResponse = getWidgetInvoiceListUseCase.getInvoices(partnerId, widgetType)
		processInvoiceListResponse(invoiceLisResponse)
		alterLoader(false)
	}

	private fun processInvoiceListResponse(result: APIResultEntity<WidgetInvoiceListEntity>) {
		result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) { response ->
			pushWidgetDetailScreenEvent(response.invoiceList)
			viewModelState.update {
				it.copy(
					isLoading = false,
					invoiceList = mapper.toWidgetInvoiceListViewData(response.invoiceList),
					interestPerDay = response.interestPerDay,
					orderBlockingDays = response.orderBlockingDays,
					showBlockOrdering = response.ledgerStatus == LedgerStatus.OVERDUE,
					ledgerOverdueAmount = response.ledgerOverdueAmount?.toString()
						?.getAmountInRupees(),
					bottomBarData = BottomBarData(
						type = bottomBarType, amount = amount, date = date
					)
				)
			}
		}
	}

	private fun pushWidgetDetailScreenEvent(response: List<InvoiceEntity>) {
		if (widgetType == InvoiceListFlowType.OVERDUE) {
			analytics.onOverdueWidgetDetailScreenViewed(
				getCount(response, InvoiceStatus.OVERDUE),
				getCount(response, InvoiceStatus.OVERDUE_START_DAYS),
				getAmount(response, InvoiceStatus.OVERDUE),
				getAmount(response, InvoiceStatus.OVERDUE_START_DAYS)
			)
		} else {
			analytics.onInterestWidgetDetailScreenViewed(
				getCount(response, InvoiceStatus.INTEREST_STARTED),
				getCount(response, InvoiceStatus.INTEREST_START_DAYS),
				getAmount(response, InvoiceStatus.INTEREST_STARTED),
				getAmount(response, InvoiceStatus.INTEREST_START_DAYS),
				isFinancedDc
			)
		}
	}

	private fun getCount(response: List<InvoiceEntity>, status: String) =
		response.filter { it.invoiceStatus == status }.size

	private fun getAmount(response: List<InvoiceEntity>, status: String) =
		response.filter { it.invoiceStatus == status }.foldRight(0.0) { invoiceEntity, acc ->
			acc + invoiceEntity.totalInvoiceAmount
		}

	private fun sendShowSnackBarEvent(message: String) {
		viewModelScope.launch {
			_uiEvent.emit(UiEvent.ShowSnackBar(message))
		}
	}

	private fun alterLoader(showLoader: Boolean) = viewModelState.update {
		it.copy(isLoading = showLoader)
	}

	fun onPayNowClicked() {
		analytics.onPayNowClicked(if (widgetType == InvoiceListFlowType.OVERDUE) PayNowScreenType.OVERDUE_WIDGET else PayNowScreenType.INTEREST_WIDGET)
	}

	companion object {
		fun getArgs(flowType: String, amount: Double, date: String, bottomBarType: String) =
			bundleOf(
				FLOW_TYPE to flowType,
				LedgerConstants.AMOUNT to amount,
				LedgerConstants.DATE to date,
				LedgerConstants.BOTTOM_BAR_TYPE to bottomBarType
			)
	}
}