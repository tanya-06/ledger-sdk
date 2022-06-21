package lib.dehaat.ledger.presentation.ledger.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import lib.dehaat.ledger.resources.textMedium16Sp

@Composable
fun NoDataFound() {
    Text(
        text = "No Data found",
        style = textMedium16Sp()
    )
}