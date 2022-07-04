package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.runtime.Composable
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData

@Composable
fun Header(
    creditSummaryData: CreditSummaryViewData?,
    ledgerColors: LedgerColors,
    onClickTotalOutstandingInfo: () -> Unit,
    onPayNowClick: () -> Unit,
    onPaymentOptionsClick: () -> Unit
) {
    CreditSummaryView(
        creditSummaryData = creditSummaryData,
        ledgerColors = ledgerColors,
        onClickTotalOutstandingInfo = onClickTotalOutstandingInfo,
        onPayNowClick = onPayNowClick,
        onPaymentOptionsClick = onPaymentOptionsClick
    )
}
