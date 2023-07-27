package lib.dehaat.ledger.presentation.ledger.downloadledger.annotations

import androidx.annotation.IntDef

@IntDef(DownloadLedgerFilter.YEARLY, DownloadLedgerFilter.CUSTOM)
annotation class DownloadLedgerFilter {
	companion object {
		const val YEARLY = 0
		const val CUSTOM = 1
	}
}