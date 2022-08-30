package lib.dehaat.ledger.presentation.model.outstanding

data class OverAllOutStandingDetailViewData(
    val principleOutstanding: String,
    val interestOutstanding: String,
    val overdueInterestOutstanding: String,
    val penaltyOutstanding: String,
    val undeliveredInvoices: String,
    val creditLinesUsed: List<String>,
)
