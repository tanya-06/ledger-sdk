package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class GetCreditLinesUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend operator fun invoke(partnerId: String) = repo.getCreditLines(partnerId)
}