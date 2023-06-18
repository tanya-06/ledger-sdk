package lib.dehaat.ledger.presentation.ledger.details.debithold

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetDebitDetailUseCase
import lib.dehaat.ledger.entities.detail.debit.LedgerDebitDetailEntity
import lib.dehaat.ledger.entities.detail.payment.PaymentDetailEntity
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LEDGER_ID
import lib.dehaat.ledger.presentation.LedgerConstants.UNREALIZED_PAYMENT
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.details.debithold.state.DebitHoldDetailViewModelState
import lib.dehaat.ledger.presentation.ledger.details.payments.state.PaymentDetailViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar
import javax.inject.Inject

@HiltViewModel
class DebitHoldDetailViewModel @Inject constructor(
    private val getDebitHoldDetailUseCase: GetDebitDetailUseCase,
    private val mapper: LedgerViewDataMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val ledgerId by lazy {
        savedStateHandle.get<String>(KEY_LEDGER_ID) ?: throw Exception(
            "Ledger id should not null"
        )
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val viewModelState = MutableStateFlow(DebitHoldDetailViewModelState())
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
            updateProgressDialog(true)
            val response = getDebitHoldDetailUseCase.invoke(ledgerId)
            updateProgressDialog(false)
            processDebitHoldDetailResponse(response)
        }
    }

    private fun processDebitHoldDetailResponse(result: APIResultEntity<LedgerDebitDetailEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) { entity ->
            val paymentDetailViewData = mapper.toDebitHoldDetailViewData(entity)
            viewModelState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true,
                    debitHoldDetailViewData = paymentDetailViewData
                )
            }
        }
    }

    private fun sendShowSnackBarEvent(message: String) {
        updateAPIFailure()
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackbar(message))
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
        fun getDebitHoldArgs(ledgerId: String) = Bundle().apply {
            putString(KEY_LEDGER_ID, ledgerId)
        }
    }
}
