package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository

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
