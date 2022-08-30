package lib.dehaat.ledger.framework.model.detail.invoice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseInvoiceDetail(
    @Json(name = "data")
    val invoiceDetailData: InvoiceDetailData
)
