package lib.dehaat.ledger.framework.model.detail.invoice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsInfo(
    @Json(name = "count")
    val count: String,
    @Json(name = "discount")
    val discount: String,
    @Json(name = "gst")
    val gst: String,
    @Json(name = "item_total")
    val itemTotal: String,
    @Json(name = "sub_total")
    val subTotal: String,
    @Json(name = "product_list")
    val productList: List<Product>
)
