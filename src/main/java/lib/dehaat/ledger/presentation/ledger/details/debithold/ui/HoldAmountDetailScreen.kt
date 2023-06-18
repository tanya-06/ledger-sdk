package lib.dehaat.ledger.presentation.ledger.details.debithold.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.model.detail.debit.LedgerDebitHoldDetailViewData
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.TertiaryYellowP20
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun HoldAmountDetailScreen(debitHoldDetail: LedgerDebitHoldDetailViewData) {

	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.wrapContentHeight()
			.verticalScroll(
				state = scrollState,
				enabled = true,
			)
			.background(Color.White)
	) {
		HoldAmountComponent(debitHoldDetail)
	}
}


@Composable
fun HoldAmountComponent(debitHoldDetail: LedgerDebitHoldDetailViewData) {

	Column(
		modifier = Modifier
			.background(color = Color.White)
			.padding(top = 24.dp, bottom = 16.dp, start = 20.dp, end = 20.dp)
			.fillMaxWidth()
			.wrapContentHeight()
	) {
		Tag(stringResource(id = R.string.prepaid_order))

		Row(
			modifier = Modifier
				.padding(top = 24.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				painter = painterResource(id = R.drawable.ic_yellow_info),
				contentDescription = "info"
			)
			Text(
				text = stringResource(id = R.string.this_amount_will_used_in_order),
				modifier = Modifier.padding(start = 4.dp),
				style = text14Sp(
					textColor = Neutral90,
					lineHeight = 20.sp,
					fontWeight = FontWeight.W500
				)
			)
		}
		PairText(
			modifier = Modifier.padding(top = 20.dp),
			key = stringResource(id = R.string.amount),
			value = debitHoldDetail.amount.getAmountInRupees()
		)
		PairText(
			modifier = Modifier.padding(top = 12.dp),
			key = stringResource(id = R.string.date),
			value = debitHoldDetail.date.toDateMonthYear()
		)
		PairText(
			modifier = Modifier.padding(top = 12.dp),
			key = stringResource(R.string.related_order_id),
			value = debitHoldDetail.orderRequestId
		)
	}
}


@Composable
fun Tag(tag: String) {
	Text(
		text = tag,
		modifier = Modifier
			.background(color = TertiaryYellowP20, shape = RoundedCornerShape(4.dp))
			.padding(horizontal = 16.dp, vertical = 4.dp)
			.wrapContentWidth(),
		style = text14Sp(
			textColor = Neutral90,
			lineHeight = 20.sp,
			fontWeight = FontWeight.W500
		)
	)
}

@Composable
fun PairText(modifier: Modifier = Modifier, key: String, value: String) {
	Row(modifier = modifier) {
		Text(
			text = key,
			modifier = Modifier.weight(1f),
			style = text14Sp(
				textColor = Neutral90,
				lineHeight = 20.sp,
				fontWeight = FontWeight.W500
			)
		)

		Text(
			text = value,
			modifier = Modifier.weight(1f),
			style = text14Sp(
				textColor = Neutral90,
				lineHeight = 18.sp,
				fontWeight = FontWeight.W600
			),
			textAlign = TextAlign.End
		)
	}
}