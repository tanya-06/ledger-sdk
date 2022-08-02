package lib.dehaat.ledger.presentation.ledger.credits.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.textBold14Sp

@Preview(
    name = "available credit limit DBA",
    showBackground = true
)
@Composable
fun AvailableCreditLimitViewScreenDBA() {
    AvailableCreditLimitView(
        limitInRupees = "10000",
        ledgerColors = DBAColors()
    )
}

@Preview(
    name = "available credit limit AIMS",
    showBackground = true
)
@Composable
fun AvailableCreditLimitViewScreenAIMS() {
    AvailableCreditLimitView(
        limitInRupees = "10000",
        ledgerColors = AIMSColors()
    )
}

@Composable
fun AvailableCreditLimitView(
    limitInRupees: String,
    ledgerColors: LedgerColors
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_credt_coin),
            contentDescription = "Coin"
        )

        Text(
            modifier = Modifier
                .padding(start = 9.dp),
            text = "Available Credit Limit",
            style = text12Sp(textColor = ledgerColors.LenderNameColor),
            maxLines = 1
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 9.dp),
            text = limitInRupees,
            style = textBold14Sp(textColor = ledgerColors.TransactionDateColor),
            maxLines = 1,
            textAlign = TextAlign.End
        )
    }
}
