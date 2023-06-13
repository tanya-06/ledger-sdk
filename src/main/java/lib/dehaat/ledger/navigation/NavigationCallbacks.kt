package lib.dehaat.ledger.navigation

import android.os.Bundle

interface DetailPageNavigationCallback {


	fun navigateToInvoiceListPage(args: Bundle)



	fun navigateToRevampInvoiceDetailPage(args: Bundle)

	fun navigateToRevampCreditNoteDetailPage(args: Bundle)

	fun navigateToRevampPaymentDetailPage(args: Bundle)



	fun navigateToHoldAmountDetailPage(args: Bundle)

    fun navigateToDebitHoldPaymentDetailPage(args: Bundle)

    fun navigateToWalletLedger(args: Bundle)
	fun navigateToWidgetInvoiceListScreen(args: Bundle)
}
