package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.data.ILedgerRepository

class GetInvoiceListUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend fun getInvoices(
        partnerId: String,
        limit: Int,
        offset: Int,
        isInterestApproached: Boolean
    ) = repo.getInterestApproachedInvoices(
        partnerId,
        limit,
        offset,
        isInterestApproached
    )
}
