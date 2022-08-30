package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetTransactionsUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend operator fun invoke(
        partnerId: String,
        limit: Int,
        offset: Int,
        onlyPenaltyInvoices: Boolean,
        toDate: Long?,
        fromDate: Long?,
    ) = repo.getTransactions(
        partnerId = partnerId,
        limit = limit,
        offset = offset,
        onlyPenaltyInvoices = onlyPenaltyInvoices,
        fromDate = fromDate,
        toDate = toDate
    )
}
