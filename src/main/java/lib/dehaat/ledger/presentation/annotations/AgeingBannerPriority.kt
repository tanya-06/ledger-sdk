package lib.dehaat.ledger.presentation.annotations

import androidx.annotation.StringDef

@StringDef(
	AgeingBannerPriority.PRIORITY_0,
	AgeingBannerPriority.PRIORITY_1,
	AgeingBannerPriority.PRIORITY_2,
	AgeingBannerPriority.PRIORITY_3
)
annotation class AgeingBannerPriority {
	companion object {
		const val PRIORITY_0 = "P0"
		const val PRIORITY_1 = "P1"
		const val PRIORITY_2 = "P2"
		const val PRIORITY_3 = "P3"
	}
}