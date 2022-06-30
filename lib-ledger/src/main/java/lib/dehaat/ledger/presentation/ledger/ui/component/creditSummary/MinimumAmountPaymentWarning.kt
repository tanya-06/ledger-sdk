package lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.initializer.getAmountInRupees
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthName
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.resources.textMedium14Sp

@Preview(
    name = "Minimum Payment Warning",
    showBackground = true
)
@Composable
private fun MinimumAmountPaymentWarningPreview() {
    MinimumAmountPaymentWarning(
        creditSummaryData = DummyDataSource.creditSummaryViewData,
        ledgerColors = AIMSColors()
    )
}

@Composable
fun MinimumAmountPaymentWarning(
    creditSummaryData: CreditSummaryViewData?,
    ledgerColors: LedgerColors
) = Column(
    modifier = Modifier.padding(
        top = 12.dp,
        bottom = 16.dp,
        start = 32.dp,
        end = 32.dp
    )
) {
    Row {
        Text(
            modifier = Modifier.weight(1f),
            text = "Minimum amount to be paid",
            style = text14Sp(textColor = ledgerColors.CtaDarkColor)

        )
        Text(
            modifier = Modifier,
            text = creditSummaryData?.overdue?.minPaymentAmount.getAmountInRupees(),
            style = textMedium14Sp(textColor = ledgerColors.CtaDarkColor)
        )
    }

    Text(
        modifier = Modifier,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = ledgerColors.CtaDarkColor,
                    fontSize = 14.sp
                )
            ) { append("By ") }

            withStyle(
                style = SpanStyle(
                    color = ledgerColors.ErrorColor,
                    fontSize = 14.sp
                )
            ) { append(creditSummaryData?.overdue?.minPaymentDueDate.toDateMonthName()) }
        }
    )

    Text(
        modifier = Modifier
            .padding(top = 8.dp),
        text = "Pay the minimum amount to enjoy \nuninterrupted ordering",
        style = text12Sp(textColor = ledgerColors.SummaryColor)
    )
}
