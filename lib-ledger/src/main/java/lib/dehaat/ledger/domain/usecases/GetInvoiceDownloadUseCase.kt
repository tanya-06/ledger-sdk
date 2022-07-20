package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class GetInvoiceDownloadUseCase @Inject constructor(
    val repo: ILedgerRepository
) {
    suspend fun invoke(
        identityId: String,
        source: String
    ) = repo.getInvoiceDownload(
        identityId,
        source
    )
}
