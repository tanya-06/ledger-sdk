package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.resources.textMedium16Sp

@Composable
fun CreditSummaryView(
    creditSummaryData: CreditSummaryViewData?,
    ledgerColors: LedgerColors,
    onClickTotalOutstandingInfo: () -> Unit,
    onPayNowClick: () -> Unit
) {
    Column(
        Modifier
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
        InfoOrderBlockPayImmediate(ledgerColors = ledgerColors)
        HeaderTotalOutstanding(
            creditSummaryData = creditSummaryData,
            ledgerColors = ledgerColors,
            onClickTotalOutstandingInfo = onClickTotalOutstandingInfo
        )
        PayNowButton(
            modifier = Modifier.padding(bottom = 12.dp, end = 18.dp),
            ledgerColors = ledgerColors,
            onPayNowClick = onPayNowClick
        )
        Divider(thickness = 4.dp, color = ledgerColors.CreditViewHeaderDividerBColor)
    }
}

@Composable
fun PayNowButton(
    modifier: Modifier = Modifier,
    ledgerColors: LedgerColors,
    onPayNowClick: () -> Unit
) {

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Text(
            modifier = Modifier
                .clickable { onPayNowClick() }
                .background(
                    color = ledgerColors.DownloadInvoiceColor,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(vertical = 12.dp, horizontal = 20.dp),
            text = "Pay Now",
            style = textMedium16Sp(textColor = Color.White)
        )
    }
}