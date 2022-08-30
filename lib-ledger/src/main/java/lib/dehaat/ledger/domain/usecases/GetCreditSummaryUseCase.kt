package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetCreditSummaryUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend operator fun invoke(partnerId: String) = repo.getCreditSummary(partnerId)
}
