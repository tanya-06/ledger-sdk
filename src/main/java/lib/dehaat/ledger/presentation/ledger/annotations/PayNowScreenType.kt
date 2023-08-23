package lib.dehaat.ledger.presentation.ledger.annotations

annotation class PayNowScreenType {
	companion object{
		const val LEDGER = "Ledger"
		const val OVERDUE_WIDGET = "Overdue Widget"
		const val INTEREST_WIDGET = "Interest Widget"
		const val ABS = "ABS"
		const val CART = "Cart"
	}
}