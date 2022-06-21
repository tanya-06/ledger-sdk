package lib.dehaat.ledger.presentation.ledger.details.creditnote.state

import lib.dehaat.ledger.presentation.model.detail.creditnote.CreditNoteDetailViewData

data class CreditNoteDetailViewModelState(
    val isLoading: Boolean = false,
    val creditNoteDetailViewData: CreditNoteDetailViewData? = null
) {
    fun toUiState() = CreditNoteDetailUIState(
        isLoading = isLoading,
        creditNoteDetailViewData = creditNoteDetailViewData
    )
}

data class CreditNoteDetailUIState(
    val isLoading: Boolean,
    val creditNoteDetailViewData: CreditNoteDetailViewData?
)