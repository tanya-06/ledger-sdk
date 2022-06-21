package lib.dehaat.ledger.initializer

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun String?.getAmountInRupeesWithoutDecimal(): String {
    val value = this?.toDoubleOrNull()
    return String.format("%s%s", "₹", formatDecimal(value, 0))
}

fun String?.getAmountInRupees(): String {
    val value = this?.toDoubleOrNull()
    return String.format("%s%s", "₹", formatDecimal(value))
}

fun Long?.toDateMonthName(): String {
    return format("dd MMM", this)
}

fun Long?.toDateMonthYearTime(): String {
    return format("dd/MM/yyyy hh:MM a", this)
}

fun Long?.toDateMonthYear(): String {
    return format("dd/MM/yyyy", this)
}

fun format(dateFormat: String, timeInSec: Long?) = try {
    timeInSec?.let {
        val sdf = SimpleDateFormat(dateFormat, Locale("en", "in"))
        sdf.format(it * 1000)
    } ?: ""
} catch (ex: Exception) {
    ex.printStackTrace()
    timeInSec.toString()
}

fun formatDecimal(value: Double?, fractionDigitCount: Int = 2): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "in"))
    formatter.minimumFractionDigits = fractionDigitCount
    formatter.maximumFractionDigits = fractionDigitCount
    return formatter.format(value ?: 0.0)
}