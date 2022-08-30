package lib.dehaat.ledger.framework.model.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseCreditSummary(
    @Json(name = "data")
    val data: CreditSummaryData
)
