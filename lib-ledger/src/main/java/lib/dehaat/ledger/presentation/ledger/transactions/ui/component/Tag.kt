package lib.dehaat.ledger.presentation.ledger.transactions.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.textMedium14Sp

@Composable
fun Tag(value: String, modifier: Modifier = Modifier, ledgerColors: LedgerColors) {
    Text(
        modifier = modifier
            .background(shape = RoundedCornerShape(18.dp), color = ledgerColors.TagBGColorDefault)
            .border(
                border = BorderStroke(width = 1.dp, color = ledgerColors.TagBorderColorDefault),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
        text = value,
        style = textMedium14Sp(textColor = ledgerColors.TagBorderColorDefault),
        maxLines = 1,
    )

}
