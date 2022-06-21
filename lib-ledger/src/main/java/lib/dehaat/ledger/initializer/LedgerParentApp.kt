package lib.dehaat.ledger.initializer

import lib.dehaat.ledger.initializer.callbacks.LedgerCallbacks
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors

sealed class LedgerParentApp(val ledgerColors: LedgerColors) {
    class AIMS(ledgerColors: LedgerColors = AIMSColors()) : LedgerParentApp(ledgerColors)
    class DBA(ledgerColors: LedgerColors = DBAColors(), val ledgerCallBack: LedgerCallbacks) : LedgerParentApp(ledgerColors)
}