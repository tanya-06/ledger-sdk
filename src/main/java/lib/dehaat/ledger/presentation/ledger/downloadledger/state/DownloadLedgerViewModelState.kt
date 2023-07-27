package lib.dehaat.ledger.presentation.ledger.downloadledger.state

import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import lib.dehaat.ledger.util.getCurrentMonth
import lib.dehaat.ledger.util.getCurrentYear
import lib.dehaat.ledger.util.getMonthList

data class DownloadLedgerViewModelState(
	val selectedDateType: String = "",
	val showDatePicker: Boolean = false,
	val monthList: List<String> = getMonthList(),
	val yearsList: List<String> = emptyList(),
	val yearsRangeList: List<DateRange> = emptyList(),
	val selectedMonth: Int = getCurrentMonth(),
	val selectedYear: Int = getCurrentYear(),
	val selectedDateRangeIndex: Int = -1,
	val showYearRangeList: Boolean = false
) {
	fun toUIState() =
		DownloadLedgerUIState(
			selectedDateType, showDatePicker, monthList, selectedMonth, yearsList,
			yearsRangeList, selectedDateRangeIndex, showYearRangeList
		)

}


