package lib.dehaat.ledger.presentation.component

import android.os.Bundle
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dehaat.androidbase.helper.isTrue
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.ui.component.OverduePaymentView
import lib.dehaat.ledger.presentation.ledger.ui.component.PaymentButton
import lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary.BlockOrderingWidget
import lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary.InterestNotStartedWidget
import lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary.InterestWidget
import lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary.OverdueWidget
import lib.dehaat.ledger.presentation.ledger.ui.component.onClickType
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.model.revamp.WidgetsViewData
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.util.FillMaxWidthColumn

@Composable
fun LedgerHeaderScreen(
	widgetsViewData: WidgetsViewData?,
	totalOutstandingAmount: String,
	onTotalOutstandingClick: onClickType,
	onPayNowClick: () -> Unit,
	onWidgetClicked: (Bundle) -> Unit
) = FillMaxWidthColumn {

	val lineStart = remember { mutableStateOf(0f) }

	WidgetsView(widgetsViewData, lineStart, onWidgetClicked)

	VerticalSpacer(height = 16.dp)

	TotalOutstandingCard(
		totalOutstanding = totalOutstandingAmount,
		onClick = onTotalOutstandingClick
	)

	if (LedgerSDK.isDBA) {
		VerticalSpacer(height = 16.dp)
		PaymentButton(modifier = Modifier.padding(horizontal = 20.dp), payNowClick = onPayNowClick)
		VerticalSpacer(height = 16.dp)
		Divider(modifier = Modifier.height(16.dp), color = Neutral10)
	}

}

@Composable
fun WidgetsView(
	widgetsViewData: WidgetsViewData?,
	lineStart: MutableState<Float>,
	onWidgetClicked: (Bundle) -> Unit
) {
	widgetsViewData?.also {
		when {
			it.creditLineStatus == LedgerConstants.ON_HOLD -> OverduePaymentView(
				it.creditLineSubStatus, it.agedOutstandingAmount, it.repaymentUnblockAmount, it.ageingBannerPriority
			)

			it.showOrderingBlockedWidget.isTrue() -> BlockOrderingWidget(
				amount = it.ledgerOverdueAmount,
				date = it.ledgerEarliestOverdueDate,
				onClick = onWidgetClicked
			)

			it.showOverdueWidget.isTrue() -> OverdueWidget(
				amount = it.ledgerOverdueAmount,
				date = it.ledgerEarliestOverdueDate,
				onClick = onWidgetClicked,
				lineStart = lineStart
			)
		}
	}

	if (widgetsViewData?.showInterestWidget.isTrue()) {
		InterestWidget(
			amount = widgetsViewData?.ledgerInterestAmount.orZero(),
			onClick = onWidgetClicked,
			date = widgetsViewData?.ledgerEarliestInterestDate.orEmpty(),
			lineStart = lineStart
		)
	}

	if (widgetsViewData?.showInterestNotStartedWidget.isTrue()) {
		InterestNotStartedWidget(
			amount = widgetsViewData?.ledgerInterestAmount.orZero(),
			onClick = onWidgetClicked,
			date = widgetsViewData?.ledgerEarliestInterestDate.orEmpty(),
			lineStart = lineStart
		)
	}
}

