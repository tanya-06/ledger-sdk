package lib.dehaat.ledger.core.callbacks

interface PaymentCallback {
    fun onClickPay(data: PaymentRequestData)
}

data class PaymentRequestData(val amount: Int)