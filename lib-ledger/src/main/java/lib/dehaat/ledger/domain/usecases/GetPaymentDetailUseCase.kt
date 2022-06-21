package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class GetPaymentDetailUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend operator fun invoke(
        ledgerId: String,
        locusId: String?,
        erpId: String?,
        mode: String?
    ) = repo.getPaymentDetail(
        ledgerId = ledgerId,
        locusId = locusId,
        erpId = erpId,
        mode = mode
    )
}