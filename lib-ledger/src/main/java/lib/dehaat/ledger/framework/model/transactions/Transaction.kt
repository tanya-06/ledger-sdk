package lib.dehaat.ledger.framework.model.transactions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Transaction(
    @Json(name = "ledger_id")
    val ledgerId: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "date")
    val date: Long,
    @Json(name = "amount")
    val amount: String,
    @Json(name = "erp_id")
    val erpId: String?,
    @Json(name = "locus_id")
    val locusId: String?,
    @Json(name = "credit_note_reason")
    val creditNoteReason: String?,
    @Json(name = "payment_mode")
    val paymentMode: String?
)