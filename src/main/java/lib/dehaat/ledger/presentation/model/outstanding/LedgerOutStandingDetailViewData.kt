package lib.dehaat.ledger.presentation.model.outstanding

data class LedgerOutStandingDetailViewData(
    val outstanding: String,
    val principleOutstanding: String,
    val interestOutstanding: String,
    val overdueInterestOutstanding: String,
    val penaltyOutstanding: String,
    val advanceAmount: String,
    val sanctionedCreditLimit: String,
    val lenderName: String,
)
