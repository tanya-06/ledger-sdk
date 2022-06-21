package lib.dehaat.ledger.presentation.ledger.details.payments.state

import lib.dehaat.ledger.presentation.model.detail.payment.PaymentDetailSummaryViewData

data class PaymentDetailViewModelState(
    val isLoading: Boolean = false,
    val paymentDetailSummaryViewData: PaymentDetailSummaryViewData? = null
) {
    fun toUiState() = PaymentDetailUIState(
        isLoading = isLoading,
        paymentDetailSummaryViewData = paymentDetailSummaryViewData
    )
}

data class PaymentDetailUIState(
    val isLoading: Boolean,
    val paymentDetailSummaryViewData: PaymentDetailSummaryViewData?
)