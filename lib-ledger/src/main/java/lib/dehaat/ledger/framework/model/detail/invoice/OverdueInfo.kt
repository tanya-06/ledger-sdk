package lib.dehaat.ledger.framework.model.detail.invoice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OverdueInfo(
    @Json(name = "overdue_date")
    val overdueDate: Long?
)
