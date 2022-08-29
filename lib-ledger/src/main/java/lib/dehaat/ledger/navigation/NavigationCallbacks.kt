package lib.dehaat.ledger.navigation

import android.os.Bundle

interface DetailPageNavigationCallback {
    fun navigateToInvoiceDetailPage(args: Bundle)

    fun navigateToCreditNoteDetailPage(args: Bundle)

    fun navigateToPaymentDetailPage(args: Bundle)
}
