package lib.dehaat.ledger.framework.model.revamp.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditV2(
    @Json(name = "buffer_limit")
    val bufferLimit: String,
    @Json(name = "credit_note_amount_till_date")
    val creditNoteAmountTillDate: String,
    @Json(name = "external_financier_supported")
    val externalFinancierSupported: Boolean,
    @Json(name = "interest_till_date")
    val interestTillDate: String,
    @Json(name = "min_interest_amount_due")
    val minInterestAmountDue: String,
    @Json(name = "min_interest_outstanding_date")
    val minInterestOutstandingDate: Long,
    @Json(name = "min_outstanding_amount_due")
    val minOutstandingAmountDue: String,
    @Json(name = "payment_amount_till_date")
    val paymentAmountTillDate: String,
    @Json(name = "permanent_credit_limit")
    val permanentCreditLimit: String,
    @Json(name = "purchase_amount_till_date")
    val purchaseAmountTillDate: String,
    @Json(name = "total_available_credit_limit")
    val totalAvailableCreditLimit: String,
    @Json(name = "total_credit_limit")
    val totalCreditLimit: String,
    @Json(name = "total_outstanding_amount")
    val totalOutstandingAmount: String,
    @Json(name = "total_purchase_amount")
    val totalPurchaseAmount: String,
    @Json(name = "undelivered_invoice_amount")
    val undeliveredInvoiceAmount: String,
    @Json(name = "total_interest_outstanding")
    val totalInterestOutstanding: String?,
    @Json(name = "total_interest_paid")
    val totalInterestPaid: String?,
    @Json(name = "minimum_repayment_amount")
    val minimumRepaymentAmount: String?,
    @Json(name = "repayment_date")
    val repaymentDate: Long?,
    @Json(name = "overdue_amount")
    val overdueAmount: String?,
    @Json(name = "overdue_credit_limit")
    val overdueCreditLimit: String?,
    @Json(name = "credit_line_status")
    val creditLineStatus: String?,
    @Json(name = "credit_line_sub_status")
    val creditLineSubStatus: String,
    @Json(name = "aged_outstanding_amount")
    val agedOutstandingAmount: Double?,
    @Json(name = "repayment_unblock_amount")
    val repaymentUnblockAmount: Double?,
    @Json(name = "repayment_unblock_days")
    val repaymentUnblockDays: Long?
)
