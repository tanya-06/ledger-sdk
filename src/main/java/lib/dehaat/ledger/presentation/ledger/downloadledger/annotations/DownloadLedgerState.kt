package lib.dehaat.ledger.presentation.ledger.downloadledger.annotations

import androidx.annotation.StringDef

@StringDef(DownloadLedgerState.FAILED, DownloadLedgerState.PROGRESS, DownloadLedgerState.SUCCESS)
annotation class DownloadLedgerState {
	companion object {
		const val PROGRESS = "progress"
		const val SUCCESS = "success"
		const val FAILED = "failed"
	}
}