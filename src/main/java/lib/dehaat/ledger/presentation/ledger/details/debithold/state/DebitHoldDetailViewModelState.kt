package lib.dehaat.ledger.presentation.ledger.details.debithold.state

import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.detail.debit.LedgerDebitHoldDetailViewData
import lib.dehaat.ledger.presentation.model.detail.payment.PaymentDetailSummaryViewData

data class DebitHoldDetailViewModelState(
    val isLoading: Boolean = false,
    val debitHoldDetailViewData: LedgerDebitHoldDetailViewData? = null,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    fun toUiState() = PaymentDetailUIState(
        isLoading = isLoading,
        debitHoldDetailViewData = debitHoldDetailViewData,
        state = when {
            isSuccess -> UIState.SUCCESS
            isError -> UIState.ERROR(errorMessage)
            isLoading -> UIState.LOADING
            else -> UIState.SUCCESS
        }
    )
}

data class PaymentDetailUIState(
    val isLoading: Boolean,
    val debitHoldDetailViewData: LedgerDebitHoldDetailViewData?,
    val state: UIState
)
