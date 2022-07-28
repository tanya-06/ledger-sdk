package lib.dehaat.ledger.entities.detail.invoice

data class ProductsInfoEntity(
    val count: String,
    val discount: String,
    val gst: String,
    val itemTotal: String,
    val subTotal: String,
    val productList: List<ProductEntity>
)
