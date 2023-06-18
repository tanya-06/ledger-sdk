package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

class GetDebitDetailUseCase @Inject constructor(val repo: ILedgerRepository) {
	suspend operator fun invoke(ledgerId: String) = repo.getDebitRecordDetails(ledgerId)
}
