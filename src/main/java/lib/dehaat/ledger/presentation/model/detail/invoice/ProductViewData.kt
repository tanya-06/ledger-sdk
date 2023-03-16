package lib.dehaat.ledger.presentation.model.detail.invoice

data class ProductViewData(
    val fname: String?,
    val name: String,
    val priceTotal: String,
    val priceTotalDiscexcl: String,
    val quantity: String
)