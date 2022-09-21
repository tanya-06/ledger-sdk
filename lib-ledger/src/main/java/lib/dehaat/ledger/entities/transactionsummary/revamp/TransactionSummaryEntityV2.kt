package lib.dehaat.ledger.entities.transactionsummary.revamp

data class TransactionSummaryEntityV2(
    val purchaseAmount: String,
    val paymentAmount: String,
    val interestAmount: String?
)
