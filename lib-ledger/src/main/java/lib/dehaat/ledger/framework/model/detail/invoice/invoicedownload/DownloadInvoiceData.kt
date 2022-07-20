package lib.dehaat.ledger.framework.model.detail.invoice.invoicedownload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DownloadInvoiceData(
    @Json(name = "source")
    val source: String,
    @Json(name = "pdf")
    val pdf: String?,
    @Json(name = "store_fname")
    val fileName: String?,
    @Json(name = "doctype")
    val docType: String?
)
