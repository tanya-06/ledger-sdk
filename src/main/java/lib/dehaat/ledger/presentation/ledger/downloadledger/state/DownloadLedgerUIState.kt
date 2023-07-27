package lib.dehaat.ledger.presentation.ledger.downloadledger.state

import lib.dehaat.ledger.presentation.model.downloadledger.DateRange

data class DownloadLedgerUIState(
	val selectedDateType: String,
	val showDatePicker: Boolean,
	val monthList: List<String>,
	val selectedMonth: Int,
	val yearsList: List<String>,
	val yearsRangeList: List<DateRange>,
	val selectedDateRangeIndex: Int,
	val showYearRangeList: Boolean
)