package lib.dehaat.ledger.util

import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerConstants.END_MONTH
import lib.dehaat.ledger.presentation.LedgerConstants.MAX_MONTHS
import lib.dehaat.ledger.presentation.LedgerConstants.START_MONTH
import lib.dehaat.ledger.presentation.LedgerConstants.START_MONTH_FOR_LIST
import lib.dehaat.ledger.presentation.LedgerConstants.UTC_TIMEZONE
import lib.dehaat.ledger.presentation.LedgerConstants.VALUE_1
import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun getYearsList(startTime: Long): List<String> {
	val startCalendar = Calendar.getInstance().apply {
		timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
		time = Date(startTime)
	}
	val endCalendar = Calendar.getInstance()
	endCalendar.set(Calendar.MONTH, END_MONTH)
	endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
	return mutableListOf<String>().apply {
		while (endCalendar.after(startCalendar)) {
			add(startCalendar.get(Calendar.YEAR).toString())
			startCalendar.add(Calendar.YEAR, VALUE_1)
		}
	}
}

fun getMonthList(): List<String> = with(Calendar.getInstance()) {
	val formatter = SimpleDateFormat(LedgerConstants.MMM, Locale.getDefault())
	set(Calendar.MONTH, START_MONTH_FOR_LIST)
	return (VALUE_1..MAX_MONTHS).map { formatter.format(time).also { add(Calendar.MONTH, 1) } }
}

fun getCurrentMonth(): Int = Calendar.getInstance().run { get(Calendar.MONTH) }

fun getCurrentYear(): Int = Calendar.getInstance().run { get(Calendar.YEAR) }

fun getTimeFromMonthYear(monthYear: Pair<Int, Int>, isFrom: Boolean) =
	with(Calendar.getInstance()) {
		set(Calendar.MONTH, monthYear.first)
		set(Calendar.YEAR, monthYear.second)
		set(Calendar.DAY_OF_MONTH, if (isFrom) VALUE_1 else getActualMaximum(Calendar.DAY_OF_MONTH))
		time.time.div(1000)
	}

fun getYearsRangeList(startTime: Long, ledgerEndDate: Long?): List<DateRange> {
	val startCalendar = Calendar.getInstance().apply {
		timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
		time = Date(startTime)
	}
	startCalendar.set(Calendar.MONTH, START_MONTH)
	startCalendar.set(Calendar.DAY_OF_MONTH, VALUE_1)
	val endCalendar = Calendar.getInstance()
	endCalendar.timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
	return mutableListOf<DateRange>().apply {
		while (endCalendar.after(startCalendar)) {
			val year = startCalendar.get(Calendar.YEAR)
			val startDate = startCalendar.time.time
			startCalendar.add(Calendar.YEAR, VALUE_1)
			startCalendar.add(Calendar.DAY_OF_MONTH, -VALUE_1)
			val nextYear = startCalendar.get(Calendar.YEAR)
			val isCurrentYear = year == getCurrentYear()
			val currentYearEndDate = if (isCurrentYear) ledgerEndDate else null
			add(
				DateRange(
					"$year - $nextYear",
					startDate.div(1000),
					currentYearEndDate ?: startCalendar.time.time.div(1000),
					startCalendar.time.time.div(1000),
					currentYearEndDate
				)
			)
			startCalendar.add(Calendar.DAY_OF_MONTH, VALUE_1)
		}
		reverse()
	}
}

fun getEndYearRange(ledgerEndDate: Long? = null): DateRange {
	val calendar = Calendar.getInstance()
	calendar.timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
	calendar.set(Calendar.MONTH, START_MONTH)
	calendar.set(Calendar.DAY_OF_MONTH, VALUE_1)
	val year = calendar.get(Calendar.YEAR)
	val startDate = calendar.time.time.div(1000)
	calendar.add(Calendar.YEAR, VALUE_1)
	calendar.add(Calendar.DAY_OF_MONTH, -VALUE_1)
	val nextYear = calendar.get(Calendar.YEAR)
	return DateRange(
		"$year - $nextYear",
		startDate,
		ledgerEndDate ?: calendar.time.time.div(1000),
		calendar.time.time.div(1000),
		ledgerEndDate
	)
}

fun getFinancialMonthStart() = Calendar.getInstance().run {
	timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
	set(Calendar.MONTH, START_MONTH)
	time.time
}

fun getMonthYear(time: Long): String {
	val formatter = SimpleDateFormat(LedgerConstants.MM_YYYY, Locale.getDefault())
	formatter.timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
	return formatter.format(Date(time))
}
