package lib.dehaat.ledger.presentation.ledger.downloadledger.annotations

import androidx.annotation.StringDef

@StringDef(SelectDateType.FROM, SelectDateType.TO)
annotation class SelectDateType {
	companion object {
		const val FROM = "from"
		const val TO = "to"
	}
}