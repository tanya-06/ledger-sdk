package lib.dehaat.ledger.presentation.ledger.credits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetCreditLinesUseCase
import lib.dehaat.ledger.entities.creditlines.CreditLineEntity
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_PARTNER_ID
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.credits.state.CreditLinesViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.processAPIResponseWithFailureSnackBar
import javax.inject.Inject

@HiltViewModel
class LedgerCreditViewModel @Inject constructor(
    private val getCreditLinesUseCase: GetCreditLinesUseCase,
    private val mapper: LedgerViewDataMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val partnerId by lazy {
        savedStateHandle.get<String>(KEY_PARTNER_ID) ?: throw Exception(
            "Partner id should not null"
        )
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val viewModelState = MutableStateFlow(CreditLinesViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        getCreditLinesFromServer()
    }

    private fun getCreditLinesFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getCreditLinesUseCase.invoke(partnerId = partnerId)
            calledAPI()
            processCreditLinesResponse(response)
        }
    }

    private fun processCreditLinesResponse(result: APIResultEntity<List<CreditLineEntity>>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) { lines ->
            viewModelState.update {
                it.copy(
                    isLoading = false,
                    creditLinesViewData = mapper.toCreditLinesViewData(lines)
                )
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

    fun showAvailableCreditLimitInfoModal() {
        viewModelState.update {
            it.copy(showAvailableCreditLimitInfoModal = true)
        }
    }

    fun hideAvailableCreditLimitInfoModal() {
        viewModelState.update {
            it.copy(showAvailableCreditLimitInfoModal = false)
        }
    }

    fun showAvailableCreditLimitInfoForLmsAndNonLmsUseModal() {
        viewModelState.update {
            it.copy(showAvailableCreditLimitInfoForLmsAndNonLmsUseModal = true)
        }
    }

    fun hideAvailableCreditLimitInfoForLmsAndNonLmsUseModal() {
        viewModelState.update {
            it.copy(showAvailableCreditLimitInfoForLmsAndNonLmsUseModal = false)
        }
    }

}