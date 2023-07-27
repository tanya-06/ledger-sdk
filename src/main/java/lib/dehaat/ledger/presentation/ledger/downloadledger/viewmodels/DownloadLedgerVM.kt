package lib.dehaat.ledger.presentation.ledger.downloadledger.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.SelectDateType
import lib.dehaat.ledger.presentation.ledger.downloadledger.state.DownloadLedgerViewModelState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.util.getCurrentMonth
import lib.dehaat.ledger.util.getCurrentYear
import lib.dehaat.ledger.util.getYearsList
import lib.dehaat.ledger.util.getYearsRangeList
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DownloadLedgerVM @Inject constructor() : ViewModel() {

	private val viewModelState = MutableStateFlow(DownloadLedgerViewModelState())
	val uiState = viewModelState.map { it.toUIState() }.stateIn(
		viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUIState()
	)

	fun updateYearsList(startYear: Long, ledgerEndDate: Long?) = viewModelState.update {
		it.copy(
			yearsList = getYearsList(startYear),
			yearsRangeList = getYearsRangeList(startYear, ledgerEndDate),
			selectedDateRangeIndex = 0
		)
	}

	fun selectDate(selectDateType: String) = viewModelState.update {
		it.copy(selectedDateType = selectDateType, showDatePicker = true)
	}

	fun dismissDialog() = viewModelState.update {
		it.copy(showDatePicker = false)
	}

	fun onMonthSelected(selectedMonth: Int) = viewModelState.update {
		it.copy(selectedMonth = selectedMonth)
	}

	fun onYearSelected(selectedYearIndex: Int) = viewModelState.update {
		it.copy(selectedYear = it.yearsList[selectedYearIndex].toInt())
	}

	fun getMonthYearPair() = viewModelState.value.run {
		selectedMonth to selectedYear
	}

	fun validateMonthYearPair(uiState: DownloadLedgerState): Boolean = when {
		viewModelState.value.selectedDateType == SelectDateType.FROM && uiState.toDate != null -> {
			validateFromToDate(
				viewModelState.value.selectedMonth to viewModelState.value.selectedYear,
				uiState.toDate
			)
		}

		viewModelState.value.selectedDateType == SelectDateType.TO && uiState.fromDate != null -> {
			validateFromToDate(
				uiState.fromDate,
				viewModelState.value.selectedMonth to viewModelState.value.selectedYear,
			)
		}

		else -> true
	}

	private fun validateFromToDate(startDate: Pair<Int, Int>, endDate: Pair<Int, Int>): Boolean {
		val fromCalendar = Calendar.getInstance().apply {
			set(Calendar.DAY_OF_MONTH, 1)
			set(Calendar.MONTH, startDate.first)
			set(Calendar.YEAR, startDate.second)
		}
		val toCalendar = Calendar.getInstance().apply {
			set(Calendar.MONTH, endDate.first)
			set(Calendar.YEAR, endDate.second)
			set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
		}
		return toCalendar.after(fromCalendar)
	}

	fun updateSelectedDateRange(selectedIndex: Int) = viewModelState.update {
		it.copy(selectedDateRangeIndex = selectedIndex)
	}

	fun alterDateRangeList() = viewModelState.update {
		it.copy(showYearRangeList = !it.showYearRangeList)
	}

	fun initData() = viewModelState.update {
		it.copy(
			selectedMonth = getCurrentMonth(),
			selectedYear = getCurrentYear()
		)
	}

}