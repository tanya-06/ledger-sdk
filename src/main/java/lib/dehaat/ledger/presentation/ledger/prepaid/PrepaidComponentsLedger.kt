package lib.dehaat.ledger.presentation.ledger.prepaid

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LibLedgerAnalytics
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldAmountViewData
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.resources.text16Sp
import lib.dehaat.ledger.resources.text18Sp
import lib.dehaat.ledger.resources.textNoto12Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun HoldAmountWidget(
	holdAmount: HoldAmountViewData,
	ledgerAnalytics: LibLedgerAnalytics,
	openHoldAmountDetailScreen: (Bundle) -> Unit
) {
	Column(
		modifier = Modifier
			.clickable {
				openHoldAmountDetailScreen(
					bundleOf(
						LedgerConstants.ABS_AMOUNT to holdAmount.absViewData.absHoldBalance.orZero(),
						LedgerConstants.KEY_PREPAID_HOLD_AMOUNT to holdAmount.prepaidHoldAmount.orZero(),
					)
				)
			}
			.background(Color.White)
			.padding(16.dp)
	) {
		Row(
			modifier = Modifier,
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				painter = painterResource(id = R.drawable.ic_lock_orange_bg),
				contentDescription = "Hold Money"
			)
			Column(
				modifier = Modifier
					.padding(start = 8.dp)
					.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.hold_balance),
					style = text14Sp(
						lineHeight = 20.sp,
						fontWeight = FontWeight.W500,
						textColor = Neutral80
					)
				)

				Text(
					text = holdAmount.formattedTotalHoldBalance,
					modifier = Modifier.padding(top = 4.dp),
					style = text18Sp(
						fontWeight = FontWeight.W500,
						textColor = Neutral90
					)
				)

			}
			Image(
				painter = painterResource(id = R.drawable.ic_black_right_arrow),
				contentDescription = "Show Details"
			)
		}

		Row(modifier = Modifier.padding(top = 12.dp)) {
			Text(text = stringResource(R.string.hold_balance_widget_info),
				style = textNoto12Sp(
					textColor = Neutral90,
					fontWeight = FontWeight.W400,
					lineHeight = 16.sp)
			)
		}

	}

	LaunchedEffect(Unit) {
		ledgerAnalytics.onHoldAmountWidgetViewed()
	}
}

@Composable
fun HoldPrepaidAmountDetailWidget(amount: Double) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(Color.White)
			.padding(22.dp)

	) {
		Text(
			text = stringResource(id = R.string.prepaid_order),
			style = text16Sp(
				lineHeight = 24.sp,
				fontWeight = FontWeight.W500,
				textColor = Neutral90
			)
		)

		Text(
			text = amount.getAmountInRupees(),
			modifier = Modifier.padding(top = 4.dp),
			style = TextStyle(
				lineHeight = 32.sp,
				fontWeight = FontWeight.W600,
				color = Neutral80,
				fontSize = 24.sp
			)
		)

		Row(
			modifier = Modifier.padding(top = 16.dp),
		) {
			Image(
				modifier = Modifier.padding(top = 4.dp),
				painter = painterResource(id = R.drawable.ic_yellow_info),
				contentDescription = "Hold Money"
			)

			Text(
				text = stringResource(id = R.string.hold_money_release_info),
				modifier = Modifier.padding(start = 6.dp, top = 2.dp),
				style = text14Sp(
					lineHeight = 18.sp,
					fontWeight = FontWeight.W400,
					textColor = Neutral70
				)
			)
		}
	}
}