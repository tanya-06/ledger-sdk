package lib.dehaat.ledger.presentation.ledger.invoicelist.state

import lib.dehaat.ledger.presentation.model.widgetinvoicelist.WidgetInvoiceViewData

data class WidgetInvoiceListVMState(
	val isLoading: Boolean = false,
	val interestPerDay: Double? = null,
	val invoiceList: List<WidgetInvoiceViewData> = emptyList(),
	val orderBlockingDays: Int? = null,
	val showBlockOrdering: Boolean = false,
	val ledgerOverdueAmount: String? = null,
	val bottomBarData: BottomBarData? = null,
	val widgetType: String? = null
) {

	fun toUiState() =
		WidgetInvoiceListUIState(
			isLoading,
			interestPerDay,
			invoiceList,
			orderBlockingDays,
			showBlockOrdering,
			ledgerOverdueAmount,
			bottomBarData,
			widgetType
		)
}

data class BottomBarData(val type: String, val amount: Double, val date: String)