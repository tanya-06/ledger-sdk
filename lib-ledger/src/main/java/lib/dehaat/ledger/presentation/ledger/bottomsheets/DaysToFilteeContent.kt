package lib.dehaat.ledger.presentation.ledger.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.common.uicomponent.SpaceMedium
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.resources.text18Sp
import lib.dehaat.ledger.resources.textMedium18Sp

@Composable
fun DaysToFilterContent(
    selectedFilter: DaysToFilter?,
    ledgerColors: LedgerColors,
    onFilterSelected: (DaysToFilter) -> Unit
) {
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
            text = "Filter",
            style = text18Sp(fontWeight = FontWeight.Bold)
        )

        SpaceMedium()
        DayFilterView(
            "All",
            DaysToFilter.All,
            selectedFilter == DaysToFilter.All,
            ledgerColors,
            onFilterSelected
        )
        SpaceMedium()
        DayFilterView(
            "7 Days",
            DaysToFilter.SevenDays,
            selectedFilter == DaysToFilter.SevenDays,
            ledgerColors, onFilterSelected
        )
        SpaceMedium()
        DayFilterView(
            "1 Month",
            DaysToFilter.OneMonth,
            selectedFilter == DaysToFilter.OneMonth,
            ledgerColors, onFilterSelected
        )
        SpaceMedium()
        DayFilterView(
            "3 Months",
            DaysToFilter.ThreeMonth,
            selectedFilter == DaysToFilter.ThreeMonth,
            ledgerColors, onFilterSelected
        )
        SpaceMedium()
        SpaceMedium()
    }
}

@Composable
fun DayFilterView(
    value: String,
    daysToFilterValue: DaysToFilter,
    selected: Boolean,
    ledgerColors: LedgerColors,
    onFilterSelected: (DaysToFilter) -> Unit
) {
    Text(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onFilterSelected(daysToFilterValue)
            },
        text = value,
        style = if (selected) text18Sp(ledgerColors.DownloadInvoiceColor) else textMedium18Sp(
            ledgerColors.CtaDarkColor
        ),
        textAlign = TextAlign.Center
    )
}