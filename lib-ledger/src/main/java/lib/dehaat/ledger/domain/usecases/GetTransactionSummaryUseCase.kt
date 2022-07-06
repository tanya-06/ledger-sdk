package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class GetTransactionSummaryUseCase @Inject constructor(
    private val repo: ILedgerRepository
) {
    suspend operator fun invoke(partnerId: String) = repo.getTransactionSummary(partnerId)
}
