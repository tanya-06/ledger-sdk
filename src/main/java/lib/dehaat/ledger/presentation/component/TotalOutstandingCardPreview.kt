package lib.dehaat.ledger.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.ledger.ui.component.onClickType
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.SeaGreen110
import lib.dehaat.ledger.resources.textHeadingH3
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.util.getRoundedAmountInRupees
import lib.dehaat.ledger.util.toDoubleOrZero

@Preview(showBackground = true)
@Composable
private fun TotalOutstandingCardPreview() = LedgerTheme {
	TotalOutstandingCard(totalOutstanding = "100"){}
}


@Preview(showBackground = true)
@Composable
private fun TotalOutstandingCard1Preview() = LedgerTheme {
	TotalOutstandingCard(totalOutstanding = "-100"){}
}

@Composable
fun TotalOutstandingCard(
	totalOutstanding: String,
	onClick: onClickType
) = Row(
	modifier = Modifier
		.padding(horizontal = 20.dp)
		.padding(horizontal = 8.dp)
		.clickable(onClick = onClick),
	verticalAlignment = Alignment.CenterVertically
) {
	Text(
		text = stringResource(id = R.string.total_outstanding),
		style = textParagraphT1Highlight(Neutral90)
	)

	HorizontalSpacer(width = 8.dp)

	Text(
		text = totalOutstanding.getRoundedAmountInRupees(),
		style = textHeadingH3(
			textColor = if (totalOutstanding.toDoubleOrZero() < 0) SeaGreen110 else Neutral80
		)
	)

	HorizontalSpacer(width = 8.dp)

	Icon(
		painter = painterResource(id = R.drawable.ic_transaction_arrow_right),
		contentDescription = ""
	)
}
