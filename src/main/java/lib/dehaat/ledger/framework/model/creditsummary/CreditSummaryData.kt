package lib.dehaat.ledger.framework.model.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditSummaryData(
    @Json(name = "credit")
    val credit: Credit,
    @Json(name = "overdue")
    val overdue: Overdue,
    @Json(name = "info")
    val info: Info
)
