package lib.dehaat.ledger.framework.model.transactions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseTransactions(
    @Json(name = "data")
    val transactionsData: TransactionsData
)
