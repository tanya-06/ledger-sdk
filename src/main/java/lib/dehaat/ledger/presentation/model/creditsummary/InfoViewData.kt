package lib.dehaat.ledger.presentation.model.creditsummary

data class InfoViewData(
    val totalPurchaseAmount: String,
    val totalPaymentAmount: String,
    val undeliveredInvoiceAmount: String,
    val firstLedgerEntryDate: Long?,
    val ledgerEndDate: Long?
)