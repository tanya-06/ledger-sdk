package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.TransactionSummaryViewData
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Primary20
import lib.dehaat.ledger.resources.Pumpkin120
import lib.dehaat.ledger.resources.SeaGreen110
import lib.dehaat.ledger.resources.TextLightGrey
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textParagraphT2
import lib.dehaat.ledger.resources.textParagraphT2Highlight

@Composable
fun TotalOutstandingCalculation(
    viewModel: RevampLedgerViewModel,
    daysToFilter: Pair<DaysToFilter, Int?>
) = Card(
    modifier = Modifier
        .fillMaxWidth(),
    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    backgroundColor = Color.White,
    elevation = 8.dp
) {
    val uiState by viewModel.transactionUIState.collectAsState()

    if (uiState.state is UIState.SUCCESS) {
        uiState.summaryViewData?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                if (daysToFilter.first != DaysToFilter.All) {
                    CalculationWithoutWeeklyInterest(it, daysToFilter)
                } else {
                    CalculationWithWeeklyInterest(it)
                }
            }
        }
    }
}

@Composable
private fun CalculationWithWeeklyInterest(
    summaryViewData: TransactionSummaryViewData
) = Column(modifier = Modifier.fillMaxWidth()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_idea_bulb),
            contentDescription = stringResource(id = R.string.accessibility_icon)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(id = R.string.total_outstanding_calculation_method),
            style = textParagraphT2(TextLightGrey)
        )
    }

    Row(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(id = R.string.total_outstanding_footer),
            style = textParagraphT2Highlight(Neutral90),
            textAlign = TextAlign.Center
        )
        Icon(
            modifier = Modifier.padding(horizontal = 4.dp),
            painter = painterResource(id = R.drawable.ic_equal),
            contentDescription = stringResource(id = R.string.accessibility_icon)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_bracket),
                contentDescription = stringResource(id = R.string.accessibility_icon),
                tint = Pumpkin120
            )
            HorizontalSpacer(width = 4.dp)
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = stringResource(id = R.string.total_purchase_rs),
                    style = textCaptionCP1(Pumpkin120)
                )
                Text(
                    text = summaryViewData.purchaseAmount,
                    style = textParagraphT2(Pumpkin120)
                )
            }

            Icon(
                modifier = Modifier.padding(horizontal = 4.dp),
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = stringResource(id = R.string.accessibility_icon),
                tint = Pumpkin120
            )
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = stringResource(id = R.string.interest_rs),
                    style = textCaptionCP1(Pumpkin120)
                )
                Text(
                    text = summaryViewData.interestAmount,
                    style = textParagraphT2(Pumpkin120)
                )
            }

            HorizontalSpacer(width = 4.dp)
            Icon(
                painter = painterResource(id = R.drawable.ic_right_bracket),
                contentDescription = stringResource(id = R.string.accessibility_icon),
                tint = Pumpkin120
            )
        }

        Icon(
            modifier = Modifier.padding(horizontal = 8.dp),
            painter = painterResource(id = R.drawable.ledger_minus),
            contentDescription = stringResource(id = R.string.accessibility_icon),
            tint = SeaGreen110
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(id = R.string.payment_rs),
                style = textCaptionCP1(SeaGreen110)
            )
            Text(
                text = summaryViewData.paymentAmount,
                style = textParagraphT2(SeaGreen110)
            )
        }
    }
}

@Composable
private fun CalculationWithoutWeeklyInterest(
    summaryViewData: TransactionSummaryViewData,
    daysToFilter: Pair<DaysToFilter, Int?>
) = Column(modifier = Modifier.fillMaxWidth()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_idea_bulb),
            contentDescription = stringResource(id = R.string.accessibility_icon)
        )

        Spacer(modifier = Modifier.width(8.dp))

        daysToFilter.second?.let {
            Text(
                text = stringResource(
                    id = R.string.filtered_total_outstanding_calculation_method,
                    it
                ),
                style = textParagraphT2(TextLightGrey)
            )
        }
    }

    VerticalSpacer(height = 13.dp)

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(id = R.string.total_purchase_rs),
                style = textCaptionCP1(Pumpkin120)
            )
            Text(
                text = summaryViewData.purchaseAmount,
                style = textParagraphT2(Pumpkin120)
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = Primary20
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(id = R.string.payment_rs),
                style = textCaptionCP1(SeaGreen110)
            )
            Text(
                text = summaryViewData.paymentAmount,
                style = textParagraphT2(SeaGreen110)
            )
        }
    }

    VerticalSpacer(height = 9.dp)
}
