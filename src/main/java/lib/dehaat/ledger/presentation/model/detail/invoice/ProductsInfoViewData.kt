package lib.dehaat.ledger.presentation.model.detail.invoice

data class ProductsInfoViewData(
    val count: String,
    val discount: String,
    val gst: String,
    val itemTotal: String,
    val subTotal: String,
    val productList: List<ProductViewData>
)