package lib.dehaat.ledger.presentation.common

import android.os.Bundle
import androidx.navigation.NavType

val NavDoubleType: NavType<Double> = object : NavType<Double>(false) {
	override val name: String
		get() = "double"

	override fun put(bundle: Bundle, key: String, value: Double) {
		bundle.putDouble(key, value)
	}

	@Suppress("DEPRECATION")
	override fun get(bundle: Bundle, key: String): Double {
		return bundle[key] as Double
	}

	override fun parseValue(value: String): Double {
		return value.toDouble()
	}
}