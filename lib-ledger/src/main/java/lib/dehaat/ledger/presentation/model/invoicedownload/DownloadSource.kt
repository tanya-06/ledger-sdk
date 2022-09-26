package lib.dehaat.ledger.presentation.model.invoicedownload

import androidx.annotation.StringDef
import lib.dehaat.ledger.presentation.model.invoicedownload.DownloadSource.Companion.ODOO
import lib.dehaat.ledger.presentation.model.invoicedownload.DownloadSource.Companion.SAP

@Retention(AnnotationRetention.SOURCE)
@StringDef(SAP, ODOO)
annotation class DownloadSource {
	companion object {
		const val SAP = "SAP"
		const val ODOO = "ODOO"
	}
}
