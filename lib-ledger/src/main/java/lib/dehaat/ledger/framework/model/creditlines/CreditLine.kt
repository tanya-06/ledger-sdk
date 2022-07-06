package lib.dehaat.ledger.framework.model.creditlines

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditLine(
    @Json(name = "belongs_to_gapl")
    val belongsToGapl: Boolean,
    @Json(name = "lender_view_name")
    val lenderViewName: String,
    @Json(name = "credit_limit")
    val creditLimit: String,
    @Json(name = "available_credit_limit")
    val availableCreditLimit: String,
    @Json(name = "total_outstanding_amount")
    val totalOutstandingAmount: String,
    @Json(name = "principal_outstanding_amount")
    val principalOutstandingAmount: String,
    @Json(name = "interest_outstanding_amount")
    val interestOutstandingAmount: String,
    @Json(name = "overdue_interest_outstanding_amount")
    val overdueInterestOutstandingAmount: String,
    @Json(name = "penalty_outstanding_amount")
    val penaltyOutstandingAmount: String,
    @Json(name = "total_advance_amount")
    val totalAdvanceAmount: String
)