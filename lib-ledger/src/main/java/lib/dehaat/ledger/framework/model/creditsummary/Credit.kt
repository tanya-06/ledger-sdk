package lib.dehaat.ledger.framework.model.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Credit(
    @Json(name = "external_financier_supported")
    val externalFinancierSupported: Boolean,
    @Json(name = "total_credit_limit")
    val totalCreditLimit: String,
    @Json(name = "total_available_credit_limit")
    val totalAvailableCreditLimit: String,
    @Json(name = "total_outstanding_amount")
    val totalOutstandingAmount: String,
    @Json(name = "principal_outstanding_amount")
    val principalOutstandingAmount: String,
    @Json(name = "interest_outstanding_amount")
    val interestOutstandingAmount: String,
    @Json(name = "overdue_interest_outstanding_amount")
    val overdueInterestOutstandingAmount: String,
    @Json(name = "penalty_outstanding_amount")
    val penaltyOutstandingAmount: String
)
