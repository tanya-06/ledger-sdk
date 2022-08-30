package lib.dehaat.ledger.framework.model.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Overdue(
    @Json(name = "total_overdue_limit")
    val totalOverdueLimit: String,
    @Json(name = "total_overdue_amount")
    val totalOverdueAmount: String,
    @Json(name = "min_payment_amount")
    val minPaymentAmount: String?,
    @Json(name = "min_payment_due_date")
    val minPaymentDueDate: Long?
)
