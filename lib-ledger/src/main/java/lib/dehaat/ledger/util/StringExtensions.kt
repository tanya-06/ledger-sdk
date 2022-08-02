package lib.dehaat.ledger.util

import lib.dehaat.ledger.initializer.formatDecimal

fun String?.nullToValue(value: String = "--") = this ?: value

fun String?.getAmountInRupeesWithoutDecimal(): String {
    val value = this?.toDoubleOrNull()
    return String.format("%s%s", "₹", formatDecimal(value, 0))
}

fun String?.getAmountInRupees(): String {
    val value = this?.toDoubleOrNull()
    return String.format("%s%s", "₹", formatDecimal(value))
}

fun String?.getAmountInRupeesOrDash(): String = this?.let {
    it.getAmountInRupees()
} ?: "-"

fun String.withArgs(
    vararg args: Any?
) = buildString {
    append(this@withArgs)
    args.forEach { arg ->
        append("/$arg")
    }
}

fun String.withArgsPath(
    vararg args: String
) = buildString {
    append(this@withArgsPath)
    args.forEach { arg ->
        append("/{$arg}")
    }
}
