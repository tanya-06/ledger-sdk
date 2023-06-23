package lib.dehaat.ledger.presentation.common

sealed class UiEvent {
    object Success : UiEvent()
    object RefreshList : UiEvent()
    data class ShowSnackBar(val message: String) : UiEvent()
}
