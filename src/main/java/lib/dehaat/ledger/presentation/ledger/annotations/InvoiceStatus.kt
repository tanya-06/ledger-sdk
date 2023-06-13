package lib.dehaat.ledger.presentation.ledger.annotations

import androidx.annotation.StringDef

@StringDef(
	InvoiceStatus.NOT_AN_INVOICE,
	InvoiceStatus.PAID,
	InvoiceStatus.OVERDUE,
	InvoiceStatus.OVERDUE_START_DAYS,
	InvoiceStatus.INTEREST_STARTED,
	InvoiceStatus.INTEREST_START_DAYS,
	InvoiceStatus.INTEREST_START_DATE
)
annotation class InvoiceStatus {
	companion object {
		const val NOT_AN_INVOICE = "NOT AN INVOICE"
		const val PAID = "PAID"
		const val OVERDUE = "OVERDUE"
		const val OVERDUE_START_DAYS = "OVERDUE_START_DAYS"
		const val INTEREST_STARTED = "INTEREST_STARTED"
		const val INTEREST_START_DAYS = "INTEREST_START_DAYS"
		const val INTEREST_START_DATE = "INTEREST_START_DATE"
	}
}