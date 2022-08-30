package lib.dehaat.ledger.entities.detail.invoice

data class ProductEntity(
    val fname: String?,
    val name: String,
    val priceTotal: String,
    val priceTotalDiscexcl: String,
    val quantity: String
)
