package lib.dehaat.ledger.presentation.ledger.details.payments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetPaymentDetailUseCase
import lib.dehaat.ledger.entities.detail.payment.PaymentDetailEntity
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_ERP_ID
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LEDGER_ID
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LOCUS_ID
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_PAYMENT_MODE
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.details.payments.state.PaymentDetailViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.processAPIResponseWithFailureSnackBar
import javax.inject.Inject

@HiltViewModel
class PaymentDetailViewModel @Inject constructor(
    private val getPaymentDetailUseCase: GetPaymentDetailUseCase,
    private val mapper: LedgerViewDataMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val ledgerId by lazy {
        savedStateHandle.get<String>(KEY_LEDGER_ID) ?: throw Exception(
            "Ledger id should not null"
        )
    }

    private val locusId by lazy { savedStateHandle.get<String>(KEY_LOCUS_ID) }

    private val erpId by lazy { savedStateHandle.get<String>(KEY_ERP_ID) }

    val paymentMode by lazy { savedStateHandle.get<String>(KEY_PAYMENT_MODE) }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val viewModelState = MutableStateFlow(PaymentDetailViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        getPaymentDetailFromServer()
    }

    private fun getPaymentDetailFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getPaymentDetailUseCase.invoke(ledgerId)
            calledAPI()
            processPaymentDetailResponse(response)
        }
    }

    private fun processPaymentDetailResponse(result: APIResultEntity<PaymentDetailEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) {
            it?.let { entity ->
                val paymentDetailViewData = mapper.toPaymentDetailSummaryViewData(entity.summary)
                viewModelState.update {
                    it.copy(isLoading = false, paymentDetailSummaryViewData = paymentDetailViewData)
                }
            }
        }
    }

    private fun sendShowSnackBarEvent(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackbar(message))
        }
    }

    private fun calledAPI() {
        viewModelState.update {
            it.copy(isLoading = false)
        }
    }

    private fun callingAPI() {
        viewModelState.update {
            it.copy(isLoading = true)
        }
    }

}