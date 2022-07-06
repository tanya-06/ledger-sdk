package lib.dehaat.ledger.framework.model.transactionsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionDetailData(
    @Json(name = "purchase_amount")
    val purchaseAmount: String,
    @Json(name = "payment_amount")
    val paymentAmount: String
)
