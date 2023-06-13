package lib.dehaat.ledger.presentation.ledger.annotations

import androidx.annotation.StringDef

@StringDef(
	ILBottomBarType.INTEREST_WILL_START,
	ILBottomBarType.INTEREST_STARTED,
	ILBottomBarType.ORDERING_WILL_BLOCKED,
	ILBottomBarType.ORDERING_BLOCKED
)
annotation class ILBottomBarType {
	companion object {
		const val INTEREST_WILL_START = "interest_will_start"
		const val INTEREST_STARTED = "interest_started"
		const val ORDERING_WILL_BLOCKED = "ordering_will_blocked"
		const val ORDERING_BLOCKED = "ordering_blocked"
	}
}