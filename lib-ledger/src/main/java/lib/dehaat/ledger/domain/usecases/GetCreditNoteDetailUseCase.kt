package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class GetCreditNoteDetailUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend operator fun invoke(
        ledgerId: String,
        locusId: String?,
        erpId: String?
    ) = repo.getCreditNoteDetail(
        ledgerId = ledgerId,
        locusId = locusId,
        erpId = erpId
    )
}