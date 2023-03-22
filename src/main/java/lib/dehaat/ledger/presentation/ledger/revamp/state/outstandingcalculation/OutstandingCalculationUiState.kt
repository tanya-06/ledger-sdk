package lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation

data class OutstandingCalculationUiState(
	val totalOutstanding: String,
	val totalPurchase: String,
	val totalPayment: String,
	val totalInvoiceAmount: String,
	val totalCreditNoteAmount: String,
	val outstandingInterestAmount: String,
	val paidInterestAmount: String,
	val creditNoteAmount: String,
	val totalDebitNoteAmount: String,
	val paidAmount: String,
	val paidRefund: String,
	val totalPaid: String
)
