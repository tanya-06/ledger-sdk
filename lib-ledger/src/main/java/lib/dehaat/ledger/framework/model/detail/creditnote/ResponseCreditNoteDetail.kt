package lib.dehaat.ledger.framework.model.detail.creditnote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseCreditNoteDetail(
    @Json(name = "data")
    val creditNoteDetailData: CreditNoteDetailData
)
