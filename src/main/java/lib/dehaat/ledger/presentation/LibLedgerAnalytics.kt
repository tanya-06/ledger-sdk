package lib.dehaat.ledger.presentation

interface LibLedgerAnalytics {
	fun onHoldAmountWidgetViewed()
	fun onHoldAmountDetailsClicked()
	fun onDownloadClicked(downloadFormat: String, timeFrameMode: String, timeFrame: String)
	fun onOverdueClicked(
		isOverdue: Boolean,
		isHomeScreen: Boolean,
		amount: Double,
		date: String?
	)

	fun onInterestClicked(
		isInterestStarted: Boolean,
		isHomeScreen: Boolean,
		amount: Double,
		date: String?,
		financed: Boolean
	)

	fun onOverdueWidgetDetailScreenViewed(
		alreadyOverdueCount: Int,
		willOverdueCount: Int,
		alreadyOverdueAmount: Double,
		willOverdueAmount: Double
	)

	fun onInterestWidgetDetailScreenViewed(
		alreadyStartedInterestCount: Int,
		willStartInterestCount: Int,
		alreadyStartedInterestAmount: Double,
		willStartInterestAmount: Double,
		isFinancedDc: Boolean
	)

	fun onInvoiceDetailViewed(
		invoiceStatus: String?,
		invoiceAmount: String?,
		outstandingAmount: String?,
		screen: String
	)

	fun onPayNowClicked(screenType: String)

	fun onPaymentStatus(screenType: String)
}