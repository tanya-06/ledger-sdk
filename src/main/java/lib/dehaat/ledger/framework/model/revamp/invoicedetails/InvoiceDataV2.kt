package lib.dehaat.ledger.framework.model.revamp.invoicedetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lib.dehaat.ledger.framework.model.detail.invoice.InterestOverDueData

@JsonClass(generateAdapter = true)
data class InvoiceDataV2(
    @Json(name = "credit_notes")
    val creditNotes: List<CreditNote>,
    @Json(name = "products_info")
    val productsInfo: ProductsInfo,
    @Json(name = "summary")
    val summary: Summary,
    @Json(name = "prepaid_and_credit_info")
    val prepaidAndCreditInfo: PrepaidAndCreditInfo?,
    @Json(name = "interestandoverdue_data")
    val interestOverdueData: InterestOverDueData?
)

@JsonClass(generateAdapter = true)
data class PrepaidAndCreditInfo(
    @Json(name = "prepaid_amount")
    val prepaidAmount: String?,
    @Json(name = "credit_amount")
    val creditAmount: String?,
)