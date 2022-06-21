package lib.dehaat.ledger.navigation

sealed class LedgerRoutes(val screen: String) {
    object LedgerDetailScreen :
        LedgerRoutes("ledger_detail_screen")

    object LedgerInvoiceDetailScreen :
        LedgerRoutes("ledger_invoice_detail_screen")

    object LedgerCreditNoteDetailScreen :
        LedgerRoutes("ledger_credit_note_detail_screen")

    object LedgerPaymentDetailScreen :
        LedgerRoutes("ledger_payment_detail_screen")

}