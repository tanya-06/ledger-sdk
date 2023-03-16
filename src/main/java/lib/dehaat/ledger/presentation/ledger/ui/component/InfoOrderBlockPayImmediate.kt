package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.resources.textMedium14Sp

@Composable
fun InfoOrderBlockPayImmediate(ledgerColors: LedgerColors) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(ledgerColors.ErrorColor)
            .padding(vertical = 10.dp, horizontal = 16.dp),
        text = "Your ordering is blocked. Pay immediately",
        style = textMedium14Sp(textColor = Color.White),
    )
}