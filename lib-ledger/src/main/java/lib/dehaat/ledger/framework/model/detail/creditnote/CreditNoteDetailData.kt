package lib.dehaat.ledger.framework.model.detail.creditnote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditNoteDetailData(
    @Json(name = "summary")
    val summary: Summary,
    @Json(name = "products_info")
    val productsInfo: ProductsInfo
)
