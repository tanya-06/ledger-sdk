package lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation

data class OutstandingCalculationUiState(
	val totalOutstanding: String,
	val totalPurchase: String,
	val totalPayment: String,
	val totalInvoiceAmount: String,
	val totalCreditNoteAmount: String,
	val showCreditNodeAmount: Boolean,
	val outstandingInterestAmount: String,
	val showOutstandingInterestAmount: Boolean,
	val paidInterestAmount: String,
	val showPaidInterestAmount:Boolean,
	val creditNoteAmount: String?,
	val totalDebitNoteAmount: String,
	val showDebitNodeAmount: Boolean,
	val paidAmount: String,
	val paidRefund: String,
	val totalPaid: String,
	val debitHold: String,
	val paymentReleased: String,
)
