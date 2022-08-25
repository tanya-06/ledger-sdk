package lib.dehaat.ledger.navigation

import android.os.Bundle

interface DetailPageNavigationCallback {
    fun navigateToInvoiceDetailPage(args: Bundle)

    fun navigateToCreditNoteDetailPage(legerId: String, erpId: String?, locusId: String?)
    fun navigateToPaymentDetailPage(
        legerId: String,
        erpId: String?,
        locusId: String?,
        mode: String?
    )
}
