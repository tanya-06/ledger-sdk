package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

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