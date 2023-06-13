package lib.dehaat.ledger.presentation.model.widgetinvoicelist

data class WidgetInvoiceViewData(
	val invoiceId: String,
	val invoiceStatus: String,
	val invoiceStatusVariable: String,
	val ledgerId: String,
	val totalInvoiceAmount: String,
	val totalRemainingAmount: String,
	val source: String,
	val erpId: String?
)