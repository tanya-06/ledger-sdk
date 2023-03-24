package lib.dehaat.ledger.framework.model.revamp.download


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseLedgerDownload(
    @Json(name = "data")
    val data: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "s3_url")
        val s3Url: String?
    )
}
