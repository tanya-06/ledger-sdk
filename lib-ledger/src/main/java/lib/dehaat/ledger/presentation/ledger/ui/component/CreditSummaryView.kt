package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary.MinimumAmountPaymentWarning
import lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary.PayNowButton
import lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary.PaymentOptionsButton
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData

@Preview(
    name = "Credit Summary AIMS",
    showBackground = true
)
@Composable
private fun CreditSummaryAIMS() {
    DummyDataSource.initAIMS(LocalContext.current)
    CreditSummaryView(
        creditSummaryData = DummyDataSource.creditSummaryViewData,
        ledgerColors = AIMSColors(),
        isLmsActivated = { false },
        onClickTotalOutstandingInfo = { },
        onPayNowClick = { },
        onPaymentOptionsClick = { }
    )
}

@Preview(
    name = "Credit Summary DBA",
    showBackground = true
)
@Composable
private fun CreditSummaryDBA() {
    DummyDataSource.initDBA(LocalContext.current)
    CreditSummaryView(
        creditSummaryData = DummyDataSource.creditSummaryViewData,
        ledgerColors = AIMSColors(),
        isLmsActivated = { false },
        onClickTotalOutstandingInfo = { },
        onPayNowClick = { },
        onPaymentOptionsClick = { }
    )
}

@Composable
fun CreditSummaryView(
    creditSummaryData: CreditSummaryViewData?,
    ledgerColors: LedgerColors,
    isLmsActivated: () -> Boolean,
    onClickTotalOutstandingInfo: () -> Unit,
    onPayNowClick: () -> Unit,
    onPaymentOptionsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ledgerColors.CreditSummaryPrimaryColor,
                        Color.White
                    )
                )
            )
            .fillMaxWidth()
    ) {
        creditSummaryData?.overdue?.minPaymentAmount?.let {
            InfoOrderBlockPayImmediate(ledgerColors = ledgerColors)
        }

        HeaderTotalOutstanding(
            creditSummaryData = creditSummaryData,
            ledgerColors = ledgerColors,
            isLmsActivated = isLmsActivated,
            onClickTotalOutstandingInfo = onClickTotalOutstandingInfo
        )

        if (LedgerSDK.isDBA) {
            Divider(modifier = Modifier, thickness = 1.dp)

            if (creditSummaryData?.overdue?.minPaymentAmount?.toDouble() != 0.0) {
                MinimumAmountPaymentWarning(
                    creditSummaryData = creditSummaryData,
                    ledgerColors = ledgerColors
                )
            }

            PayNowButton(
                ledgerColors = ledgerColors,
                onPayNowClick = onPayNowClick
            )
        }

        Divider(thickness = 4.dp, color = ledgerColors.CreditViewHeaderDividerBColor)

        if (LedgerSDK.isDBA) {
            PaymentOptionsButton(ledgerColors, onPaymentOptionsClick)
        }
    }
}
