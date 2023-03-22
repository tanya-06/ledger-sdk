package lib.dehaat.ledger.presentation.model.creditlines

data class CreditLineViewData(
    val belongsToGapl: Boolean,
    val lenderViewName: String,
    val creditLimit: String,
    val availableCreditLimit: String,
    val totalOutstandingAmount: String,
    val principalOutstandingAmount: String,
    val interestOutstandingAmount: String,
    val overdueInterestOutstandingAmount: String,
    val penaltyOutstandingAmount: String,
    val advanceAmount: String
)