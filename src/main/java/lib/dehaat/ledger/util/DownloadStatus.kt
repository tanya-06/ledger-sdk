package lib.dehaat.ledger.util

import android.app.DownloadManager
import lib.dehaat.ledger.presentation.model.invoicedownload.ProgressData

data class DownloadStatus(
    var status: Int = DownloadManager.ERROR_UNKNOWN,
    var filePath: String = "",
    var progressData: ProgressData = ProgressData()
)
