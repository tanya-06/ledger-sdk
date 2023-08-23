package lib.dehaat.ledger.presentation.ledger.invoicelist.state

import lib.dehaat.ledger.presentation.model.widgetinvoicelist.WidgetInvoiceViewData

data class WidgetInvoiceListUIState(
	val isLoading: Boolean,
	val interestPerDay: Double?,
	val invoiceList: List<WidgetInvoiceViewData>,
	val orderBlockingDays: Int?,
	val showBlockOrdering: Boolean,
	val ledgerOverdueAmount: String?,
	val bottomBarData: BottomBarData?,
	val widgetType: String?
)