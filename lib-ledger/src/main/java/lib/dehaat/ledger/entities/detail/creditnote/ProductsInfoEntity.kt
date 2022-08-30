package lib.dehaat.ledger.entities.detail.creditnote

data class ProductsInfoEntity(
    val count: Int,
    val gst: String,
    val itemTotal: String,
    val subTotal: String,
    val productList: List<ProductEntity>?
)
