package lib.dehaat.ledger.presentation.ledger.invoicelist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.ledger.annotations.ILBottomBarType
import lib.dehaat.ledger.presentation.ledger.invoicelist.state.BottomBarData
import lib.dehaat.ledger.presentation.ledger.ui.component.PaymentButton
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.resources.textSemiBold16Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun WidgetILBottomBar(bottomBarData: BottomBarData?, onPayNowClick: () -> Unit) {
	if (bottomBarData != null && LedgerSDK.isDBA) {
		Card(
			modifier = Modifier.clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
			shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
		) {
			val amountInRupees = remember(bottomBarData.amount) {
				bottomBarData.amount.toString().getAmountInRupees()
			}
			Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 16.dp)) {
				Text(
					text = stringResource(R.string.amount_paid, amountInRupees),
					style = textSemiBold16Sp(Neutral90)
				)
				Text(text = getBottomBarDesc(bottomBarData), style = textMedium14Sp(Neutral80))
				Column(modifier = Modifier.padding(top = 20.dp)) {
					PaymentButton(payNowClick = onPayNowClick)
				}
			}
		}
	}
}

@Composable
fun getBottomBarDesc(bottomBarData: BottomBarData): String = when (bottomBarData.type) {
	ILBottomBarType.INTEREST_WILL_START -> stringResource(
		R.string.pay_before_s_to_save_interest, bottomBarData.date
	)

	ILBottomBarType.INTEREST_STARTED -> stringResource(R.string.pay_today_to_save_interest)
	ILBottomBarType.ORDERING_WILL_BLOCKED -> stringResource(
		R.string.pay_before_s_avoid_block_ordering, bottomBarData.date
	)

	ILBottomBarType.ORDERING_BLOCKED -> stringResource(R.string.pay_today_unblock_ordering)
	else -> ""
}