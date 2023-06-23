package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter

class DaysToFilterUseCase @Inject constructor() {


	fun getSelectedDates(daysToFilter: DaysToFilter) = when (daysToFilter) {
		DaysToFilter.SevenDays -> calculateTime(7)
		DaysToFilter.OneMonth -> calculateTime(30)
		DaysToFilter.ThreeMonth -> calculateTime(90)
		else -> null
	}

	private fun calculateTime(dayCount: Int) = with(dayCount) {
		val daysSec = this * 24 * 60 * 60
		val currentDaySec = System.currentTimeMillis() / 1000
		val pastDaySec = currentDaySec.minus(daysSec)
		Pair(pastDaySec, currentDaySec)
	}
}
