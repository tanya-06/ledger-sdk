package lib.dehaat.ledger.framework.model.detail.invoice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Summary(
    @Json(name = "amount")
    val amount: String,
    @Json(name = "number")
    val number: String,
    @Json(name = "timestamp")
    val timestamp: Long
)
