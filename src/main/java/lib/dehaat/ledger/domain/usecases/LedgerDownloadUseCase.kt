package lib.dehaat.ledger.domain.usecases

import lib.dehaat.ledger.data.ILedgerRepository
import lib.dehaat.ledger.util.DownloadFileUtil
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar
import javax.inject.Inject

class LedgerDownloadUseCase @Inject constructor(
	private val repo: ILedgerRepository,
	private val downloadFileUtil: DownloadFileUtil
) {
	suspend operator fun invoke(
		partnerId: String,
		fromDate: Long?,
		toDate: Long?,
		format: String,
		onDownloadStarted: () -> Unit
	) = repo.downloadLedger(partnerId, fromDate, toDate, format).apply {
		processAPIResponseWithFailureSnackBar({}) {
			onDownloadStarted()
			downloadFileUtil.downloadFile(
				it,
				null,
				format
			)
		}
	}
}
