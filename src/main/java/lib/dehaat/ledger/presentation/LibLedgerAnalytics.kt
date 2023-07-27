package lib.dehaat.ledger.presentation

interface LibLedgerAnalytics {
	fun onHoldAmountWidgetViewed()
	fun onHoldAmountDetailsClicked()
	fun onDownloadClicked(downloadFormat: String, timeFrameMode: String, timeFrame: String)
}