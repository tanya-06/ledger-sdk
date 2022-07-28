package lib.dehaat.ledger.framework.model.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Info(
    @Json(name = "total_purchase_amount")
    val totalPurchaseAmount: String,
    @Json(name = "total_payment_amount")
    val totalPaymentAmount: String,
    @Json(name = "undelivered_invoice_amount")
    val undeliveredInvoiceAmount: String
)
