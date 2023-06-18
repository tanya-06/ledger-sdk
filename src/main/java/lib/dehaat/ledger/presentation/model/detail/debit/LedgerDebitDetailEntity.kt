package lib.dehaat.ledger.presentation.model.detail.debit

data class LedgerDebitHoldDetailViewData(
	val amount: String,
	val creationReason: String,
	val date: Long,
	val name: String,
	val orderRequestId: String,
)