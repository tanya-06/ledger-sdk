package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Warning10
import lib.dehaat.ledger.resources.textButtonB1
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textParagraphT1
import lib.dehaat.ledger.util.getRoundedAmountInRupees
import lib.dehaat.ledger.util.toDoubleOrZero
import lib.dehaat.ledger.util.tooltip.ToolTipOffSet
import lib.dehaat.ledger.util.tooltip.ToolTipScreen
import lib.dehaat.ledger.util.tooltip.TooltipShape
import lib.dehaat.ledger.util.tooltip.ViewOffset
import lib.dehaat.ledger.util.tooltip.model.ArrowPosition

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
