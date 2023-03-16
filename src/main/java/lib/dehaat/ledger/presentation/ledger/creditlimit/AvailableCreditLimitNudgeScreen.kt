package lib.dehaat.ledger.presentation.ledger.creditlimit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.resources.Error90
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.textParagraphT1Highlight

@Preview(
    showBackground = true
)
@Composable
private fun AvailableCreditLimitScreenPreview() = LedgerTheme {
    AvailableCreditLimitNudgeScreen("₹40,000")
}

@Composable
fun AvailableCreditLimitNudgeScreen(
    minimumRepaymentAmount: String
) = Column(
    modifier = Modifier
        .background(Error90)
        .padding(12.dp)
) {

    Text(
        text = stringResource(
            R.string.your_order_limit_is_exhausted_please_pay_minimum,
            minimumRepaymentAmount
        ),
        style = textParagraphT1Highlight(Color.White)
    )

}
