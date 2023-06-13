package lib.dehaat.ledger.framework.model.revamp.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditSummaryDataV2(
	@Json(name = "credit")
	val credit: CreditV2
)

@JsonClass(generateAdapter = true)
data class AgeingBannerMessage(
    @Json(name = "priority")
    val priority: String,
    @Json(name = "interest_per_day")
    val penaltyInterest: Float?,
    @Json(name = "overdue_amount")
    val agedOverdueAmount: Double?
)
