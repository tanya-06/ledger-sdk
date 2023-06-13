package lib.dehaat.ledger.framework.model.transactions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Transaction(
    @Json(name = "ledger_id")
    val ledgerId: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "date")
    val date: Long,
    @Json(name = "amount")
    val amount: String,
    @Json(name = "erp_id")
    val erpId: String?,
    @Json(name = "locus_id")
    val locusId: String?,
    @Json(name = "credit_note_reason")
    val creditNoteReason: String?,
    @Json(name = "payment_mode")
    val paymentMode: String?,
    @Json(name = "source")
    val source: String,
    @Json(name = "unrealized_payment")
    val unrealizedPayment: Boolean?,
    @Json(name = "interest_end_date")
    val interestEndDate: Long?,
    @Json(name = "interest_start_date")
    val interestStartDate: Long?,
    @Json(name = "partner_id")
    val partnerId: String,
    @Json(name = "source_no")
    val sourceNo: String?,
    @Json(name = "from_date")
    val fromDate: Long?,
    @Json(name = "to_date")
    val toDate: Long?,
    @Json(name = "adjustment_amount")
    val adjustmentAmount: Double?,
    @Json(name = "scheme_name")
    val schemeName: String?,
    @Json(name = "credit_amount")
    val creditAmount: String?,
    @Json(name = "prepaid_amount")
    val prepaidAmount: String?,
    @Json(name = "status")
    val invoiceStatus: String?,
    @Json(name = "status_variable")
    val statusVariable: String?,
    @Json(name = "total_invoice_amount")
    val totalInvoiceAmount: Double?,
    @Json(name = "total_interest_charged")
    val totalInterestCharged: Double?,
    @Json(name = "total_remaining_amount")
    val totalRemainingAmount: Double?
)
