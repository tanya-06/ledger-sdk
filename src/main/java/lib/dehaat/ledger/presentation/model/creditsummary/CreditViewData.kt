package lib.dehaat.ledger.presentation.model.creditsummary

data class CreditViewData(
    val externalFinancierSupported: Boolean,
    val totalCreditLimit: String,
    val totalAvailableCreditLimit: String,
    val totalOutstandingAmount: String,
    val principalOutstandingAmount: String,
    val interestOutstandingAmount: String,
    val overdueInterestOutstandingAmount: String,
    val penaltyOutstandingAmount: String
)