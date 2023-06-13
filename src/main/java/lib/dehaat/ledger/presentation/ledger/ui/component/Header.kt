package lib.dehaat.ledger.presentation.ledger.ui.component

import android.os.Bundle
import androidx.compose.runtime.Composable
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData

@Composable
fun Header(
	creditSummaryData: CreditSummaryViewData?,
	ledgerColors: LedgerColors,
	isLmsActivated: () -> Boolean?,
	onClickTotalOutstandingInfo: () -> Unit,
	onPayNowClick: () -> Unit,
	onPaymentOptionsClick: () -> Unit,
    onWidgetClick: (Bundle) -> Unit
) {
    CreditSummaryView(
        creditSummaryData = creditSummaryData,
        ledgerColors = ledgerColors,
        isLmsActivated = isLmsActivated,
        onClickTotalOutstandingInfo = onClickTotalOutstandingInfo,
        onPayNowClick = onPayNowClick,
        onPaymentOptionsClick = onPaymentOptionsClick,
        onWidgetClick = onWidgetClick
    )
}
