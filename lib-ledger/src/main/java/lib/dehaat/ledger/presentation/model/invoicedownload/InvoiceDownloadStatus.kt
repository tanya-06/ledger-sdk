package lib.dehaat.ledger.presentation.model.invoicedownload

sealed class InvoiceDownloadStatus {
    object SUCCESS : InvoiceDownloadStatus()
    object PROGRESS : InvoiceDownloadStatus()
    object ERROR : InvoiceDownloadStatus()
}
