package lib.dehaat.ledger.framework.model.creditlines

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseCreditLines(
    @Json(name = "data")
    val creditLineData: CreditLineData
)
