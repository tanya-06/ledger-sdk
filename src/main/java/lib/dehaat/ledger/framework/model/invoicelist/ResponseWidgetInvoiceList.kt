package lib.dehaat.ledger.framework.model.invoicelist


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseWidgetInvoiceList(
	@Json(name = "data")
	val data: ResponseWidgetInvoiceListData
)

@JsonClass(generateAdapter = true)
data class ResponseWidgetInvoiceListData(
	@Json(name = "interest_per_day")
	val interestPerDay: Double?,
	@Json(name = "invoice_list")
	val invoiceList: List<Invoice>?,
	@Json(name = "overdue_days")
	val orderBlockingDays: Int?,
	@Json(name = "ledger_status")
	val ledgerStatus: String?,
	@Json(name = "ledger_overdue_amount")
	val ledgerOverdueAmount: Double?

)

@JsonClass(generateAdapter = true)
data class Invoice(
	@Json(name = "invoice_id")
	val invoiceId: String,
	@Json(name = "invoice_status")
	val invoiceStatus: String,
	@Json(name = "invoice_status_variable")
	val invoiceStatusVariable: String,
	@Json(name = "ledger_id")
	val ledgerId: String,
	@Json(name = "total_invoice_amount")
	val totalInvoiceAmount: Double,
	@Json(name = "total_remaining_amount")
	val totalRemainingAmount: Double,
	@Json(name = "source")
	val source: String?,
	@Json(name = "erp_id")
	val erpId: String?,
)