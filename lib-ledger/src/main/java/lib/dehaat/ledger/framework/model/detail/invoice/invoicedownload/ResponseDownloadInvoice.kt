package lib.dehaat.ledger.framework.model.detail.invoice.invoicedownload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseDownloadInvoice(
    @Json(name = "data")
    val downloadInvoiceData: DownloadInvoiceData
)
