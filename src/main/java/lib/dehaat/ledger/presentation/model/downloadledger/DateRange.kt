package lib.dehaat.ledger.presentation.model.downloadledger

data class DateRange(
	val displayDateRange: String,
	val startDate: Long,
	val pdfEndDate: Long,
	val excelEndDate: Long,
	val ledgerEndDate: Long?
)