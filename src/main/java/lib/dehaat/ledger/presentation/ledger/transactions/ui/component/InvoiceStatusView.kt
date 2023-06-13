package lib.dehaat.ledger.presentation.ledger.transactions.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dehaat.androidbase.helper.orZero
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceStatus
import lib.dehaat.ledger.resources.Color20CEF2AA
import lib.dehaat.ledger.resources.Color20FFD9DC
import lib.dehaat.ledger.resources.Color20FFDFB2
import lib.dehaat.ledger.resources.Error10
import lib.dehaat.ledger.resources.Error110
import lib.dehaat.ledger.resources.Secondary10
import lib.dehaat.ledger.resources.Secondary110
import lib.dehaat.ledger.resources.Secondary120
import lib.dehaat.ledger.resources.Success110
import lib.dehaat.ledger.resources.Success20
import lib.dehaat.ledger.resources.textSemiBold12Sp

@Composable
fun InvoiceStatusView(status: String?, statusVariable: String?) = when (status) {
	InvoiceStatus.INTEREST_STARTED -> InterestStarted()
	InvoiceStatus.INTEREST_START_DAYS -> InterestStartDays(statusVariable)
	InvoiceStatus.OVERDUE_START_DAYS -> OverdueStartDays(statusVariable)
	InvoiceStatus.OVERDUE -> OverdueView()
	InvoiceStatus.PAID -> PaymentDoneView()
	else -> {}

}

@Preview
@Composable
private fun PaymentDoneView() = Text(
	text = stringResource(R.string.payment_done),
	style = textSemiBold12Sp(Success110),
	modifier = Modifier
		.clip(RoundedCornerShape(6.dp))
		.background(Color20CEF2AA)
		.border(1.dp, Success20)
		.padding(horizontal = 8.dp, vertical = 3.dp)
)

@Preview
@Composable
private fun OverdueView() = Text(
	text = stringResource(R.string.is_overdue),
	style = textSemiBold12Sp(Color.White),
	modifier = Modifier
		.clip(RoundedCornerShape(6.dp))
		.background(Error110)
		.padding(horizontal = 8.dp, vertical = 3.dp)
)

@Preview
@Composable
private fun OverdueStartDays(days: String? = null) = Row(
	modifier = Modifier
		.clip(RoundedCornerShape(6.dp))
		.background(Color20FFD9DC)
		.border(1.dp, Error10)
		.padding(horizontal = 8.dp, vertical = 3.dp)
) {
	Image(
		painter = painterResource(R.drawable.ic_blocked),
		contentDescription = null
	)
	Text(
		text = pluralStringResource(
			R.plurals.overdue_after_s_days,
			days?.toIntOrNull().orZero(),
			days.orEmpty()
		),
		style = textSemiBold12Sp(Error110),
		modifier = Modifier.padding(start = 4.dp)
	)
}

@Preview
@Composable
private fun InterestStartDays(days: String? = null) = Row(
	modifier = Modifier
		.clip(RoundedCornerShape(6.dp))
		.background(Color20FFDFB2)
		.border(1.dp, Secondary10)
		.padding(horizontal = 8.dp, vertical = 3.dp)
) {
	Image(
		painter = painterResource(R.drawable.ic_blocked),
		contentDescription = null,
		colorFilter = ColorFilter.tint(Secondary120)
	)
	Text(
		text = pluralStringResource(
			R.plurals.interest_start_after_s_days,
			days?.toIntOrNull().orZero(),
			days.orEmpty()
		),
		style = textSemiBold12Sp(Secondary120),
		modifier = Modifier.padding(start = 4.dp)
	)
}

@Preview
@Composable
private fun InterestStarted() = Text(
	text = stringResource(R.string.interest_applied),
	style = textSemiBold12Sp(Color.White),
	modifier = Modifier
		.clip(RoundedCornerShape(6.dp))
		.background(Secondary110)
		.padding(horizontal = 8.dp, vertical = 3.dp)
)
