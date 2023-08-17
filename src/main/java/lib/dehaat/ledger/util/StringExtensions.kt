package lib.dehaat.ledger.util

import com.dehaat.androidbase.helper.tryCatchWithReturn
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import java.text.NumberFormat
import java.util.Locale

fun String?.nullToValue(value: String = "--") = this ?: value

fun Double?.getAmountInRupees(): String {
	val isNegativeValue = this?.let { it < 0 } ?: false
	if (isNegativeValue) {
		this?.let {
			val amount = it * -1
			return String.format("%s%s", "- ₹", formatDecimal(amount))
				.getAmountWithoutTrailingZeroes()
		}
	}
	return String.format("%s%s", "₹", formatDecimal(this)).getAmountWithoutTrailingZeroes()
}

fun String?.getAmountInRupees(): String {
	val value = this?.toDoubleOrNull()
	val isNegativeValue = value?.let { it < 0 } ?: false
	if (isNegativeValue) {
		value?.let {
			val amount = it * -1
			return String.format("%s%s", "- ₹", formatDecimal(amount))
				.getAmountWithoutTrailingZeroes()
		}
	}
	return String.format("%s%s", "₹", formatDecimal(value)).getAmountWithoutTrailingZeroes()
}

fun String?.getRoundedAmountInRupees(precision: Int = 0): String {
	val value = this?.toDoubleOrNull()
	val isNegativeValue = value?.let { it < 0 } ?: false
	if (isNegativeValue) {
		value?.let {
			val amount = it * -1
			return String.format("%s%s", "- ₹", formatDecimal(amount, precision))
		}
	}
	return String.format("%s%s", "₹", formatDecimal(value, precision))
}

private fun String.getAmountWithoutTrailingZeroes() = if (this.endsWith(".00")) {
	this.substringBeforeLast(".00")
} else {
	this
}

fun String?.getAmountInRupeesOrDash(): String = this?.let {
	it.getAmountInRupees()
} ?: "-"

fun String.formatAmount() = tryCatchWithReturn(this) {
	val formatter = NumberFormat.getNumberInstance(Locale(LedgerSDK.locale, "in"))
	formatter.maximumFractionDigits = 2
	return formatter.format(toDouble())
}

fun String?.toDoubleOrZero(): Double = this?.let {
	it.toDoubleOrNull().orZero()
}.orZero()

fun String?.isNullOrZero() = toDoubleOrZero() == 0.0

fun String.appendSignIfRequired(sign: String) =
	if (contains("+") || contains("-")) this else "$sign $this"

