package lib.dehaat.ledger.presentation.ledger.details.payments

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orFalse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetPaymentDetailUseCase
import lib.dehaat.ledger.entities.detail.payment.PaymentDetailEntity
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LEDGER_ID
import lib.dehaat.ledger.presentation.LedgerConstants.UNREALIZED_PAYMENT
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.details.payments.state.PaymentDetailViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar
import javax.inject.Inject
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LMS_ACTIVATED

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

    val paymentMode by lazy { savedStateHandle.get<String>(KEY_PAYMENT_MODE) }
    val unrealizedPayment by lazy { savedStateHandle.get<Boolean>(UNREALIZED_PAYMENT) }

    private val lmsActivated: Boolean by lazy {
        savedStateHandle.get<Boolean>(KEY_LMS_ACTIVATED).orFalse()
    }

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

    fun isLmsActivated() = lmsActivated

    private fun getPaymentDetailFromServer() {
        callInViewModelScope {
            updateProgressDialog(true)
            val response = getPaymentDetailUseCase.invoke(ledgerId)
            updateProgressDialog(false)
            processPaymentDetailResponse(response)
        }
    }

    private fun processPaymentDetailResponse(result: APIResultEntity<PaymentDetailEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) { entity ->
            val paymentDetailViewData = mapper.toPaymentDetailSummaryViewData(entity.summary)
            viewModelState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true,
                    paymentDetailSummaryViewData = paymentDetailViewData
                )
            }
        }
    }

    private fun sendShowSnackBarEvent(message: String) {
        updateAPIFailure()
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackBar(message))
        }
    }

    private fun updateAPIFailure() = viewModelState.update {
        it.copy(
            isError = true,
            isLoading = false
        )
    }

    fun updateProgressDialog(show: Boolean) = viewModelState.update {
        it.copy(isLoading = show)
    }

    companion object {
        private const val KEY_PAYMENT_MODE = "KEY_PAYMENT_MODE"
        fun getArgs(
            data: TransactionViewData,
            isLMSActivated: Boolean
        ) = Bundle().apply {
            putString(KEY_LEDGER_ID, data.ledgerId)
            putString(KEY_PAYMENT_MODE, data.paymentMode)
            putBoolean(UNREALIZED_PAYMENT, data.unrealizedPayment ?: false)
            putBoolean(KEY_LMS_ACTIVATED, isLMSActivated)
        }

        fun getBundle(
            ledgerId: String,
            unrealizedPayment: Boolean?,
            isLMSActivated: Boolean
        ) = bundleOf(
            Pair(KEY_LEDGER_ID, ledgerId),
            Pair(UNREALIZED_PAYMENT, unrealizedPayment),
            Pair(KEY_LMS_ACTIVATED, isLMSActivated)
        )
    }
}
