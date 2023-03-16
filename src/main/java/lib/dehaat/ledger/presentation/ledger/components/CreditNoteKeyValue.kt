package lib.dehaat.ledger.presentation.ledger.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.resources.textBold14Sp

@Composable
fun CreditNoteKeyValue(
    key: String,
    value: String,
    modifier: Modifier = Modifier,
    keyModifier: Modifier = Modifier,
    valueModifier: Modifier = Modifier,
    keyTextStyle: TextStyle = text14Sp(),
    valueTextStyle: TextStyle = textBold14Sp(),
) {
    Row(modifier = modifier) {
        Text(
            modifier = keyModifier.weight(1f),
            text = key,
            maxLines = 1,
            style = keyTextStyle
        )

        Text(
            modifier = valueModifier,
            text = value,
            style = valueTextStyle,
            maxLines = 1
        )
    }
}

@Composable
fun CreditNoteKeyValueInSummaryView(
    key: String,
    value: String,
    ledgerColors: LedgerColors
) {
    CreditNoteKeyValue(
        key, value,
        keyTextStyle = text14Sp(textColor = ledgerColors.CtaDarkColor),
        valueTextStyle = textBold14Sp(textColor = ledgerColors.CtaDarkColor),
    )
}

@Composable
fun CreditNoteKeyValueInSummaryViewWithTopPadding(
    key: String,
    value: String,
    ledgerColors: LedgerColors,
    valueTextStyle: TextStyle = textBold14Sp(textColor = ledgerColors.CtaDarkColor),
) {
    CreditNoteKeyValue(
        key, value,
        keyTextStyle = text14Sp(textColor = ledgerColors.CtaDarkColor),
        valueTextStyle = valueTextStyle,
        modifier = Modifier.padding(top = 12.dp),
    )
}

