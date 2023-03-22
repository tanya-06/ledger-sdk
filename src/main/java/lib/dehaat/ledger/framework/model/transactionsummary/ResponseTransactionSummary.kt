package lib.dehaat.ledger.framework.model.transactionsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseTransactionSummary(
    @Json(name = "data")
    val transactionDetailData: TransactionDetailData
)
