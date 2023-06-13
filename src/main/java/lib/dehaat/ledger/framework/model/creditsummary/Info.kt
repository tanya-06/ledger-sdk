package lib.dehaat.ledger.framework.model.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Info(
	@Json(name = "total_purchase_amount")
	val totalPurchaseAmount: String,
	@Json(name = "total_payment_amount")
	val totalPaymentAmount: String,
	@Json(name = "undelivered_invoice_amount")
	val undeliveredInvoiceAmount: String,
	@Json(name = "first_ledger_entry_date")
	val firstLedgerEntryDate: Long?,
	@Json(name = "ledger_restricted_download_date")
	val ledgerEndDate: Long?,
    @Json(name = "ledger_overdue_amount")
    val ledgerOverdueAmount: Double?,
    @Json(name = "ledger_earliest_overdue_date")
    val ledgerEarliestOverdueDate: Double?,
    @Json(name = "overdue_status")
    val overdueStatus: String?
)
