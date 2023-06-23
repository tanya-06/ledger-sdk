package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.data.ILedgerRepository

class GetCreditNoteDetailUseCase @Inject constructor(
    val repo: ILedgerRepository
) {
    suspend operator fun invoke(ledgerId: String) = repo.getCreditNoteDetail(ledgerId)
}
