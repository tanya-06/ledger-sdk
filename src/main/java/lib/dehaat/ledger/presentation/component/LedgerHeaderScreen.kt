package lib.dehaat.ledger.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.ui.component.OutStandingPaymentView
import lib.dehaat.ledger.presentation.ledger.ui.component.PaymentButton
import lib.dehaat.ledger.presentation.ledger.ui.component.onClickType
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.util.FillMaxWidthColumn

@Composable
fun LedgerHeaderScreen(
	totalOutstandingAmount: String,
	onTotalOutstandingClick: onClickType,
	onPayNowClick: () -> Unit
) = FillMaxWidthColumn {
	val outstanding by LedgerSDK.outstandingDataFlow.collectAsState()
	if (outstanding.showDialog) {
		OutStandingPaymentView(outstanding)
	}

	VerticalSpacer(height = 16.dp)

	TotalOutstandingCard(
		totalOutstanding = totalOutstandingAmount,
		onClick = onTotalOutstandingClick
	)

	VerticalSpacer(height = 16.dp)

	PaymentButton(modifier = Modifier.padding(horizontal = 20.dp), payNowClick = onPayNowClick)

	VerticalSpacer(height = 16.dp)

	Divider(modifier = Modifier.height(16.dp), color = Neutral10)

}
