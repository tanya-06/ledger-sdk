package lib.dehaat.ledger.presentation.ledger.bottomsheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceMedium
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryView
import lib.dehaat.ledger.presentation.model.outstanding.OverAllOutStandingDetailViewData
import lib.dehaat.ledger.resources.text18Sp
import lib.dehaat.ledger.resources.textBold14Sp
import lib.dehaat.ledger.R

@Composable
fun OverAllOutStandingDetails(data: OverAllOutStandingDetailViewData, ledgerColors: LedgerColors) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Divider(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(60.dp),
                thickness = 6.dp,
                color = ledgerColors.CreditViewHeaderDividerBColor
            )
        }

        SpaceMedium()
        SpaceMedium()

        Text(
            modifier = Modifier,
            text = "Outstanding Detail",
            style = text18Sp(fontWeight = FontWeight.Bold)
        )

        SpaceMedium()

        CreditNoteKeyValueInSummaryView(
            "Principal o/s",
            data.principleOutstanding.getAmountInRupees(),
            ledgerColors = ledgerColors
        )

        SpaceMedium()
        CreditNoteKeyValueInSummaryView(
            "Interest o/s",
            data.interestOutstanding.getAmountInRupees(),
            ledgerColors = ledgerColors
        )

        SpaceMedium()
        CreditNoteKeyValueInSummaryView(
            "Overdue Interest o/s",
            data.overdueInterestOutstanding.getAmountInRupees(),
            ledgerColors = ledgerColors
        )

        SpaceMedium()
        CreditNoteKeyValueInSummaryView(
            "Penalty o/s",
            data.penaltyOutstanding.getAmountInRupees(),
            ledgerColors = ledgerColors
        )


        SpaceMedium()
        CreditNoteKeyValueInSummaryView(
            "Undelivered Invoices",
            data.undeliveredInvoices.getAmountInRupees(),
            ledgerColors = ledgerColors
        )

        SpaceMedium()

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = ledgerColors.CreditViewHeaderDividerBColor,
            thickness = 1.dp,
        )

        SpaceMedium()

        Text(
            modifier = Modifier,
            text = "Credit line Used",
            style = text18Sp(fontWeight = FontWeight.Bold)
        )

        SpaceMedium()
        data.creditLinesUsed.forEach {
            CreditLineUsed(it)
            SpaceMedium()
        }
        SpaceMedium()
    }
}

@Composable
fun CreditLineUsed(value: String) {

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(1f),
            text = value,
            style = textBold14Sp()
        )

        Image(painter = painterResource(id = R.drawable.ic_green_tick), contentDescription = "tick")
    }
}