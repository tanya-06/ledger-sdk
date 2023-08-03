package lib.dehaat.ledger.entities.creditsummary

data class InfoEntity(
    val totalPurchaseAmount: String,
    val totalPaymentAmount: String,
    val undeliveredInvoiceAmount: String,
    val firstLedgerEntryDate: Long?,
    val ledgerEndDate: Long?
)
