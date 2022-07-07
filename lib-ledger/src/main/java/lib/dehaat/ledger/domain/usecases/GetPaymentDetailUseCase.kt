package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class GetPaymentDetailUseCase @Inject constructor(val repo: ILedgerRepository) {
    suspend operator fun invoke(ledgerId: String) = repo.getPaymentDetail(ledgerId)
}
