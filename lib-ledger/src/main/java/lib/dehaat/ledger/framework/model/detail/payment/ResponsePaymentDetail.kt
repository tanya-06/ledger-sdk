package lib.dehaat.ledger.framework.model.detail.payment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponsePaymentDetail(
    @Json(name = "data")
    val paymentDetailData: PaymentDetailData
)
