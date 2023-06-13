package lib.dehaat.ledger.presentation.ledger.annotations

import androidx.annotation.StringDef

@StringDef(LedgerFlowType.HOME_DASHBOARD, LedgerFlowType.INVOICE_LIST)
@Retention
annotation class LedgerFlowType {
	companion object {
		const val HOME_DASHBOARD = "home"
		const val INVOICE_LIST = "invoice_list"
	}
}