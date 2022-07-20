package lib.dehaat.ledger.entities.detail.invoice.invoicedownload

data class InvoiceDownloadDataEntity(
    val source: String,
    val pdf: String?,
    val fileName: String?,
    val docType: String?
)
