package lib.dehaat.ledger.presentation.ledger.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import lib.dehaat.ledger.resources.textMedium16Sp

@Composable
fun NoDataFound() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "No Data found",
        style = textMedium16Sp()
    )
}
