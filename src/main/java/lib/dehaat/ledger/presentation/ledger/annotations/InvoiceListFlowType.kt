package lib.dehaat.ledger.presentation.ledger.annotations

import androidx.annotation.StringDef

@StringDef(InvoiceListFlowType.OVERDUE, InvoiceListFlowType.INTEREST)
annotation class InvoiceListFlowType {
	companion object {
		const val OVERDUE = "OVERDUE"
		const val INTEREST = "INTEREST"
	}
}