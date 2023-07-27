package lib.dehaat.ledger.presentation.model.downloadledger

data class DownloadStatusData(
	val status: Boolean, val filePath: String? = null, val mimeType: String? = null
)