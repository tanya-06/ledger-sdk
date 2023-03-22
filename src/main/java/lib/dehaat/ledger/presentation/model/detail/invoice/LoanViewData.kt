package lib.dehaat.ledger.presentation.model.detail.invoice

data class LoanViewData(
    val loanAccountNo: String,
    val status: String,
    val amount: String,
    val invoiceContributionInLoan: String,
    val totalOutstandingAmount: String?,
    val principalOutstandingAmount: String?,
    val interestOutstandingAmount: String?,
    val penaltyOutstandingAmount: String?,
    val overdueInterestOutstandingAmount: String?,
    val disbursalDate: Long?,
    val interestFreeEndDate: Long?,
    val financier: String,
    val belongsToGapl: Boolean
)