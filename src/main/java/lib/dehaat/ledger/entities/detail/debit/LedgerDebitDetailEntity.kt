package lib.dehaat.ledger.entities.detail.debit

data class LedgerDebitDetailEntity(
	val amount: String,
	val creationReason: String,
	val date: Long,
	val name: String,
	val orderRequestId: String,
)