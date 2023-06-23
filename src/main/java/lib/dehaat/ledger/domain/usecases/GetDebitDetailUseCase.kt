package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.data.ILedgerRepository
import javax.inject.Inject

class GetDebitDetailUseCase @Inject constructor(val repo: ILedgerRepository) {
	suspend operator fun invoke(ledgerId: String) = repo.getDebitRecordDetails(ledgerId)
}
