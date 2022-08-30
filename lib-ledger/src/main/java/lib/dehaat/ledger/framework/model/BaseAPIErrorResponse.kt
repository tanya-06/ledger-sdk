package lib.dehaat.ledger.framework.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseAPIErrorResponse(
    @Json(name = "error")
    val error: Error?
)

@JsonClass(generateAdapter = true)
data class Error(
    @Json(name = "category")
    val category: String?,
    @Json(name = "message")
    val message: String?
)
