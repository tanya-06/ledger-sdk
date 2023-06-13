package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.data.ILedgerRepository
import javax.inject.Inject

class GetWidgetInvoiceListUseCase @Inject constructor(val repo: ILedgerRepository) {
	suspend fun getInvoices(partnerId: String, widgetType: String) =
		repo.getWidgetInvoiceList(partnerId, widgetType)
}