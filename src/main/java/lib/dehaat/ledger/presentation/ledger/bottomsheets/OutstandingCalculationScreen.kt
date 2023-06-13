package lib.dehaat.ledger.presentation.ledger.bottomsheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation.OutstandingCalculationUiState
import lib.dehaat.ledger.resources.ColorEDFBFF
import lib.dehaat.ledger.resources.FrenchBlue10
import lib.dehaat.ledger.resources.FrenchBlue20
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.Neutral30
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Primary110
import lib.dehaat.ledger.resources.Pumpkin120
import lib.dehaat.ledger.resources.Secondary120
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.resources.textSubHeadingS3
import lib.dehaat.ledger.util.DottedShape

@Preview(showBackground = true)
@Composable
fun OutstandingCalculationPreview() = LedgerTheme {
	OutstandingCalculationScreen(outstandingCalculationUiState = DummyDataSource.outstandingCalculationUiState) {}
}

@Composable
fun OutstandingCalculationScreen(
	outstandingCalculationUiState: OutstandingCalculationUiState,
	showBottomSheet: () -> Unit
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.verticalScroll(rememberScrollState())
) {
	TotalOutstandingCalculation(outstandingCalculationUiState, showBottomSheet)
	Divider(modifier = Modifier.height(8.dp), color = Neutral10)
	TotalPurchasesCalculation(outstandingCalculationUiState)
	Divider(modifier = Modifier.height(8.dp), color = Neutral10)
	TotalPaymentCalculation(outstandingCalculationUiState)
}

@Composable
fun OutstandingCalculationHeader(
	outstandingCalculationUiState: OutstandingCalculationUiState,
	peekHeight: (Dp) -> Unit,
	density: Density = LocalDensity.current,
	showBottomSheet: () -> Unit
) {
	Column(
		modifier = Modifier
			.clickable(onClick = showBottomSheet)
			.fillMaxWidth()
			.onGloballyPositioned { layoutCoordinates ->
				peekHeight(with(density) { layoutCoordinates.size.height.toDp() })
			}
	) {
		Row(
			modifier = Modifier
				.height(IntrinsicSize.Min)
				.fillMaxWidth()
				.background(FrenchBlue10)
				.padding(vertical = 8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text(
					text = stringResource(R.string.ledger_total_purchase),
					style = textParagraphT2Highlight(Pumpkin120)
				)
				Text(
					text = outstandingCalculationUiState.totalPurchase,
					style = textParagraphT1Highlight(Pumpkin120)
				)
			}

			Divider(
				modifier = Modifier
					.fillMaxHeight()
					.width(1.dp),
				color = FrenchBlue20
			)

			Column(horizontalAlignment = Alignment.CenterHorizontally) {
				Text(
					text = stringResource(R.string.ledger_total_payment),
					style = textParagraphT2Highlight(Primary110)
				)
				Text(
					text = outstandingCalculationUiState.totalPayment,
					style = textParagraphT1Highlight(Primary110)
				)
			}
		}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.background(FrenchBlue20)
				.padding(vertical = 10.dp),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = stringResource(R.string.ledger_outstanding_calculation),
				style = textCaptionCP1(Neutral70)
			)

			HorizontalSpacer(width = 4.dp)

			Icon(
				modifier = Modifier
					.background(shape = CircleShape, color = FrenchBlue20),
				painter = painterResource(id = R.drawable.baseline_keyboard_arrow_up_24),
				contentDescription = "",
				tint = Neutral70
			)
		}
	}
}

@Composable
private fun CalculationKeyValuePair(
	title: String,
	value: String,
	valueColor: Color,
	keyStyle: TextStyle = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		textDecoration = TextDecoration.Underline
	)
) = Row(
	modifier = Modifier
		.fillMaxWidth()
		.padding(horizontal = 20.dp),
	horizontalArrangement = Arrangement.SpaceBetween
) {
	Text(text = title, style = keyStyle)

	Text(text = value, style = textParagraphT2Highlight(valueColor))
}

@Composable
private fun TotalOutstandingCalculation(
	outstandingCalculationUiState: OutstandingCalculationUiState,
	showBottomSheet: () -> Unit
) {
	Row(
		modifier = Modifier
			.clickable(onClick = showBottomSheet)
			.background(ColorEDFBFF)
			.padding(vertical = 8.dp, horizontal = 16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Row {
			Image(
				painter = painterResource(id = R.drawable.ic_idea_bulb),
				contentDescription = stringResource(id = R.string.accessibility_icon)
			)

			Spacer(modifier = Modifier.width(8.dp))

			Text(
				text = stringResource(R.string.ledger_outstanding_calculation),
				style = TextStyle(
					fontWeight = FontWeight.SemiBold,
					fontSize = 14.sp,
					lineHeight = 18.sp,
					textDecoration = TextDecoration.Underline
				)
			)
		}

		Surface(elevation = 4.dp, shape = CircleShape) {
			Icon(
				modifier = Modifier,
				painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
				contentDescription = "",
				tint = Neutral70
			)
		}
	}

	VerticalSpacer(height = 16.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_total_purchase),
		value = outstandingCalculationUiState.totalPurchase,
		valueColor = Secondary120
	)

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_total_payment),
		value = outstandingCalculationUiState.totalPayment,
		valueColor = Primary110
	)


	VerticalSpacer(height = 20.dp)
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = stringResource(R.string.ledger_total_outstanding),
			style = textSubHeadingS3(Neutral90)
		)

		Text(
			text = outstandingCalculationUiState.totalOutstanding,
			style = textSubHeadingS3(Neutral90)
		)
	}

	VerticalSpacer(height = 16.dp)
}

@Composable
private fun TotalPurchasesCalculation(
	outstandingCalculationUiState: OutstandingCalculationUiState
) {
	Text(
		modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp),
		text = stringResource(R.string.ledger_total_calculation),
		style = textSubHeadingS3(Neutral80)
	)

	Divider()

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_total_invoice_amount),
		value = outstandingCalculationUiState.totalInvoiceAmount,
		Pumpkin120
	)

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_total_debit_note_amount),
		value = outstandingCalculationUiState.totalDebitNoteAmount,
		Pumpkin120
	)

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_outstanding_interest_amount),
		value = outstandingCalculationUiState.outstandingInterestAmount,
		Pumpkin120
	)

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_paid_interest_amount),
		value = outstandingCalculationUiState.paidInterestAmount,
		Pumpkin120
	)

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_credit_note_interst_returned_amount),
		value = outstandingCalculationUiState.creditNoteAmount,
		Primary110
	)

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_credit_note_amount),
		value = outstandingCalculationUiState.totalCreditNoteAmount,
		Primary110
	)

	VerticalSpacer(height = 12.dp)

	Divider(
		modifier = Modifier
			.padding(horizontal = 20.dp)
			.background(
				color = Neutral30,
				shape = DottedShape(8.dp)
			)
	)

	VerticalSpacer(height = 8.dp)

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = stringResource(R.string.ledger_total_purchases),
			style = textSubHeadingS3(Neutral90)
		)

		Text(
			text = outstandingCalculationUiState.totalPurchase,
			style = textSubHeadingS3(Neutral90)
		)
	}
	VerticalSpacer(height = 16.dp)
}

@Composable
fun TotalPaymentCalculation(
	outstandingCalculationUiState: OutstandingCalculationUiState
) {
	Text(
		modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp),
		text = stringResource(R.string.ledger_total_payment_calculation),
		style = textSubHeadingS3(Neutral80)
	)

	Divider()

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_total_payment_made_by_you),
		value = outstandingCalculationUiState.paidAmount,
		Primary110
	)

	VerticalSpacer(height = 12.dp)
	CalculationKeyValuePair(
		title = stringResource(R.string.ledger_paid_refund),
		value = outstandingCalculationUiState.paidRefund,
		Pumpkin120
	)

	VerticalSpacer(height = 12.dp)

	Divider(
		modifier = Modifier
			.padding(horizontal = 20.dp)
			.background(
				color = Neutral30,
				shape = DottedShape(8.dp)
			)
	)

	VerticalSpacer(height = 8.dp)
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = stringResource(R.string.ledger_total_paid),
			style = textSubHeadingS3(Neutral90)
		)

		Text(
			text = outstandingCalculationUiState.totalPaid,
			style = textSubHeadingS3(Neutral90)
		)
	}
	VerticalSpacer(height = 16.dp)
}
