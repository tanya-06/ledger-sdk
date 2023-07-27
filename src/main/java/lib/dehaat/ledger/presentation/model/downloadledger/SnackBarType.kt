package lib.dehaat.ledger.presentation.model.downloadledger

sealed class SnackBarType {
	object DownloadFailed : SnackBarType()
	object DownloadProgress : SnackBarType()
	data class DownloadSuccess(val filePath: String, val mimeType: String?) : SnackBarType()
}