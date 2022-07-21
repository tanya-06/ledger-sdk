package lib.dehaat.ledger.initializer

import lib.dehaat.ledger.initializer.callbacks.LedgerCallbacks
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors

sealed class LedgerParentApp(
    val ledgerCallBack: LedgerCallbacks,
    val ledgerColors: LedgerColors
) {
    class AIMS(
        ledgerCallBack: LedgerCallbacks,
        ledgerColors: LedgerColors = AIMSColors()
    ) : LedgerParentApp(ledgerCallBack, ledgerColors)

    class DBA(
        ledgerCallBack: LedgerCallbacks,
        ledgerColors: LedgerColors = DBAColors()
    ) : LedgerParentApp(
        ledgerCallBack,
        ledgerColors
    )
}