package lib.dehaat.ledger.framework.model.detail.invoice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InterestOverDueData(
	@Json(name = "status")
	val invoiceStatus: String?,
	@Json(name = "status_variable")
	val statusVariable: String?,
	@Json(name = "total_invoice_amount")
	val totalInvoiceAmount: Double?,
	@Json(name = "total_interest_charged")
	val totalInterestCharged: Double?,
	@Json(name = "total_remaining_amount")
	val totalRemainingAmount: Double?,
	@Json(name = "interest_per_day")
	val interestPerDay: Double?
)