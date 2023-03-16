package lib.dehaat.ledger.presentation.ledger.details.invoice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral100
import lib.dehaat.ledger.resources.textCaptionCP1

@Preview
@Composable
private fun ChipPreview() = LedgerTheme {
    InformationChip(title = "title", backgroundColor = Neutral100, textColor = Color.White)
}

@Composable
fun InformationChip(
    title: String,
    backgroundColor: Color,
    textColor: Color
) = Column(
    modifier = Modifier
        .background(
            color = backgroundColor,
            shape = RoundedCornerShape(4.dp)
        )
        .padding(horizontal = 8.dp, vertical = 4.dp)
) {
    Text(
        text = title,
        style = textCaptionCP1(textColor)
    )
}
