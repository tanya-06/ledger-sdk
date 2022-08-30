package lib.dehaat.ledger.entities.creditsummary

data class CreditEntity(
    val externalFinancierSupported: Boolean,
    val totalCreditLimit: String,
    val totalAvailableCreditLimit: String,
    val totalOutstandingAmount: String,
    val principalOutstandingAmount: String,
    val interestOutstandingAmount: String,
    val overdueInterestOutstandingAmount: String,
    val penaltyOutstandingAmount: String
)
