package lib.dehaat.ledger.navigation

sealed class LedgerRoutes(val screen: String) {

	object LedgerHomeScreen: LedgerRoutes("ledger_home_screen")

	object InvoiceListScreen : LedgerRoutes("invoice_list_screen")

	object TotalAvailableCreditLimitScreen :
		LedgerRoutes("total_available_credit_limit_detail_screen")

	object RevampLedgerInvoiceDetailScreen :
		LedgerRoutes("revamp_ledger_invoice_detail_screen")

	object RevampLedgerCreditNoteDetailScreen :
		LedgerRoutes("revamp_ledger_credit_note_detail_screen")

	object RevampLedgerPaymentDetailScreen :
		LedgerRoutes("revamp_ledger_payment_detail_screen")

	object RevampLedgerWeeklyInterestDetailScreen :
		LedgerRoutes("revamp_ledger_weekly_interest_detail_screen")

    object ABSDetailScreen :
        LedgerRoutes("abs_detail_screen")

    object DebitHoldDetailScreen :
        LedgerRoutes("debit_hold_detail_screen")

	object WalletLedgerRoute :
		LedgerRoutes("wallet_ledger_history")

    object WidgetInvoiceListScreen : LedgerRoutes("widget_invoice_list_screen")

}
