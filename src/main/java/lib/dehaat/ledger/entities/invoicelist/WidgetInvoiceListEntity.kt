package lib.dehaat.ledger.entities.invoicelist

class WidgetInvoiceListEntity(
	val interestPerDay: Double?,
	val invoiceList: List<InvoiceEntity>,
	val orderBlockingDays: Int?,
	val ledgerStatus: String?,
	val ledgerOverdueAmount: Double?
)

data class InvoiceEntity(
	val invoiceId: String,
	val invoiceStatus: String,
	val invoiceStatusVariable: String,
	val ledgerId: String,
	val totalInvoiceAmount: Double,
	val totalRemainingAmount: Double,
	val source: String,
	val erpId: String?
)