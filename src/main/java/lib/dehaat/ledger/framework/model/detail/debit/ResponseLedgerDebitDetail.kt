package lib.dehaat.ledger.framework.model.detail.debit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseLedgerDebitDetail(
	@Json(name = "data")
	val data: LedgerData?
) {
	@JsonClass(generateAdapter = true)
	data class LedgerData(
		@Json(name = "amount")
		val amount: String?,
		@Json(name = "creation_reason")
		val creationReason: String?,
		@Json(name = "date")
		val date: Long?,
		@Json(name = "name")
		val name: String?,
		@Json(name = "order_request_id")
		val orderRequestId: String?,
	)
}