package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.resources.BgLightGreen20
import lib.dehaat.ledger.resources.Success110
import lib.dehaat.ledger.resources.text12Sp

@Preview
@Composable
fun FullyPaidTag(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .background(shape = RoundedCornerShape(4.dp), color = BgLightGreen20)
            .border(width = 1.dp, shape = RoundedCornerShape(4.dp), color = Success110)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = stringResource(id = R.string.fully_paid),
        style = text12Sp(lineHeight = 14.sp, fontWeight = FontWeight.W500, textColor = Success110)
    )
}