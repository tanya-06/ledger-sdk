package lib.dehaat.ledger.presentation.ledger.details.creditnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetCreditNoteDetailUseCase
import lib.dehaat.ledger.entities.detail.creditnote.CreditNoteDetailEntity
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_ERP_ID
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LEDGER_ID
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LOCUS_ID
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.details.creditnote.state.CreditNoteDetailViewModelState
import lib.dehaat.ledger.presentation.ledger.state.LedgerDetailViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.processAPIResponseWithFailureSnackBar
import javax.inject.Inject

@HiltViewModel
class CreditNoteDetailViewModel @Inject constructor(
    private val getCreditNoteDetailUseCase: GetCreditNoteDetailUseCase,
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

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val viewModelState = MutableStateFlow(CreditNoteDetailViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        getCreditNoteDetailFromServer()
    }

    private fun getCreditNoteDetailFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getCreditNoteDetailUseCase.invoke(ledgerId)
            calledAPI()
            processCreditNoteDetailResponse(response)
        }
    }

    private fun processCreditNoteDetailResponse(result: APIResultEntity<CreditNoteDetailEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) {
            it?.let { creditSummaryEntity ->
                val creditNoteDetailViewData = mapper.toCreditNoteDetailDataEntity(creditSummaryEntity)
                viewModelState.update { it ->
                    it.copy(isLoading = false, creditNoteDetailViewData = creditNoteDetailViewData)
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