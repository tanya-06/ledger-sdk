package lib.dehaat.ledger.presentation.ledger.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.resources.textBold14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun ProductView(
    name: String,
    image: String?,
    qty: String,
    price: String,
    ledgerColors: LedgerColors,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Row(modifier = Modifier) {
            ProductImageLedger(imgUrl = image ?: "", ledgerColors = ledgerColors)
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                text = name,
                style = text14Sp(textColor = ledgerColors.CtaColor),
                maxLines = 1
            )
            Text(
                modifier = Modifier,
                text = "Qty: $qty",
                style = text12Sp(textColor = ledgerColors.CtaColor),
                maxLines = 1
            )
        }

        Text(
            modifier = Modifier,
            text = price.getAmountInRupees(),
            style = textBold14Sp(textColor = ledgerColors.CtaColor),
            maxLines = 1
        )
    }
}
