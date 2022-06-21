package lib.dehaat.ledger.presentation.model.transactions

sealed class DaysToFilter {
    object All : DaysToFilter()
    object SevenDays : DaysToFilter()
    object OneMonth : DaysToFilter()
    object ThreeMonth : DaysToFilter()
    data class CustomDays(val fromDateMilliSec: Long, val toDateMilliSec: Long) : DaysToFilter()
}