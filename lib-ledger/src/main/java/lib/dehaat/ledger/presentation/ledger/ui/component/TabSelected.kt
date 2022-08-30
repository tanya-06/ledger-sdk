package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.textMedium14Sp

@Composable
fun TabSelected(label:String, modifier: Modifier = Modifier, ledgerColors: LedgerColors) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(shape = RoundedCornerShape(8.dp), color = ledgerColors.TabBGColorSelected)
            .border(
                border = BorderStroke(width = 1.2.dp, color = ledgerColors.TabBorderColorSelected),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            style = textMedium14Sp(textColor = ledgerColors.TabTextColorSelected),
        )
    }
}