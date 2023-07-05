package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData

typealias onClickType = () -> Unit

@Composable
fun RepaymentScreen(
	summaryViewData: SummaryViewData,
	onPayNowClick: onClickType,
	onOtherPaymentModeClick: onClickType
) = Column(
	modifier = Modifier.fillMaxWidth()
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
	) {
		if (LedgerSDK.isDBA) {
			VerticalSpacer(height = 24.dp)

			Column(modifier = Modifier.padding(horizontal = 20.dp)) {
				PaymentButton(payNowClick = onPayNowClick)
			}

			VerticalSpacer(height = 16.dp)
		}
	}

	VerticalSpacer(height = 25.dp)
}
