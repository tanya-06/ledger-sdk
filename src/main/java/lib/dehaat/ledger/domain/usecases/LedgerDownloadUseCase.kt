package lib.dehaat.ledger.domain.usecases

import javax.inject.Inject
import lib.dehaat.ledger.domain.ILedgerRepository
import lib.dehaat.ledger.util.DownloadFileUtil
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar

class LedgerDownloadUseCase @Inject constructor(
	private val repo: ILedgerRepository,
	private val downloadFileUtil: DownloadFileUtil
) {
	suspend operator fun invoke(
		partnerId: String
	) = repo.downloadLedger(partnerId).processAPIResponseWithFailureSnackBar({}) {
		downloadFileUtil.downloadExcel(
			it,
			"ledger"
		)
	}
}
