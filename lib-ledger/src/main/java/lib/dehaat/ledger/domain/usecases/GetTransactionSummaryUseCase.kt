package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetTransactionSummaryUseCase @Inject constructor(
    private val repo: ILedgerRepository
) {
    suspend fun getTransactionSummary(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ) = repo.getTransactionSummary(partnerId, fromDate, toDate)

    suspend fun getTransactionSummaryV2(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ) = repo.getTransactionSummaryV2(partnerId, fromDate, toDate)
}
