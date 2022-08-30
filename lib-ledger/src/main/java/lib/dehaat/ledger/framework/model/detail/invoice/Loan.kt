package lib.dehaat.ledger.framework.model.detail.invoice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Loan(
    @Json(name = "loan_account_no")
    val loanAccountNo: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "amount")
    val amount: String,
    @Json(name = "invoice_contribution_in_loan")
    val invoiceContributionInLoan: String,
    @Json(name = "total_outstanding_amount")
    val totalOutstandingAmount: String?,
    @Json(name = "principal_outstanding_amount")
    val principalOutstandingAmount: String?,
    @Json(name = "interest_outstanding_amount")
    val interestOutstandingAmount: String?,
    @Json(name = "penalty_outstanding_amount")
    val penaltyOutstandingAmount: String?,
    @Json(name = "overdue_interest_outstanding_amount")
    val overdueInterestOutstandingAmount: String?,
    @Json(name = "disbursal_date")
    val disbursalDate: Long?,
    @Json(name = "interest_free_end_date")
    val interestFreeEndDate: Long?,
    @Json(name = "financier")
    val financier: String,
    @Json(name = "belongs_to_gapl")
    val belongsToGapl: Boolean
)
