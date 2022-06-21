package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.getAmountInRupees
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthName
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.resources.textBold14Sp
import lib.dehaat.ledger.resources.textMedium14Sp

@Composable
fun HeaderTotalOutstanding(
    creditSummaryData: CreditSummaryViewData?,
    ledgerColors: LedgerColors,
    onClickTotalOutstandingInfo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 12.dp,
                start = 32.dp,
                end = 32.dp
            )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Outstanding",
                    style = text14Sp(textColor = ledgerColors.CtaColor)
                )
                Image(
                    modifier = Modifier
                        .padding(start = 7.dp)
                        .clickable { onClickTotalOutstandingInfo() },
                    painter = painterResource(id = R.drawable.ic_info_icon),
                    contentDescription = "info"
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = creditSummaryData?.credit?.totalOutstandingAmount.getAmountInRupees(),
                style = textBold14Sp(textColor = ledgerColors.CtaColor)
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(color = ledgerColors.ErrorLightColor)
                    .padding(2.dp),
                text = "Credit Limit Exhausted",
                style = text14Sp(textColor = ledgerColors.ErrorColor)
            )
            Text(modifier = Modifier.padding(top = 12.dp),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            color = ledgerColors.SummaryColor,
                        )
                    ) {
                        append("Includes overdue amount of ")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontSize = 14.sp,
                            color = ledgerColors.ErrorColor,
                        )
                    ) {
                        append(creditSummaryData?.overdue?.totalOverdueAmount.getAmountInRupees())
                    }
                })
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(color = ledgerColors.ErrorLightColor)
                    .padding(2.dp),
                text = "Overdue limit exhausted",
                style = text14Sp(textColor = ledgerColors.ErrorColor)
            )

        }

        Divider(modifier = Modifier, thickness = 1.dp)

        Column(
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

            Text(modifier = Modifier,
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = ledgerColors.CtaDarkColor,
                            fontSize = 14.sp
                        )
                    ) {
                        append("By ")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = ledgerColors.ErrorColor,
                            fontSize = 14.sp
                        )
                    ) {
                        append(creditSummaryData?.overdue?.minPaymentDueDate.toDateMonthName())
                    }
                })

            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                text = "Pay the minimum amount to enjoy \nuninterrupted ordering",
                style = text12Sp(textColor = ledgerColors.SummaryColor)
            )
        }

    }
}