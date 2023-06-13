package lib.dehaat.ledger.presentation.ledger.invoicelist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceStatus
import lib.dehaat.ledger.presentation.ledger.details.invoice.InvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.RevampInvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.invoicelist.state.WidgetInvoiceListUIState
import lib.dehaat.ledger.presentation.ledger.transactions.ui.component.InvoiceStatusView
import lib.dehaat.ledger.presentation.model.widgetinvoicelist.WidgetInvoiceViewData
import lib.dehaat.ledger.resources.Color20E3E3E3
import lib.dehaat.ledger.resources.Error100
import lib.dehaat.ledger.resources.Error90
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Secondary10
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textMedium12Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.resources.textSemiBold14Sp
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.toDateMonthYear

@Composable
fun WidgetInvoiceListContent(
	uiState: WidgetInvoiceListUIState,
	ledgerColors: LedgerColors,
	isDCFinanced: Boolean,
	detailPageNavigationCallback: DetailPageNavigationCallback
) = Column {
	if (uiState.interestPerDay != null) {
		Text(
			text = stringResource(R.string.interest_rate_s_per_day, uiState.interestPerDay),
			modifier = Modifier
				.fillMaxWidth()
				.background(Secondary10)
				.padding(horizontal = 20.dp, vertical = 8.dp),
			style = textSemiBold14Sp(Neutral80)
		)
	}

	if (uiState.showBlockOrdering) {
		ILBlockOrderingWidget(amountInRupees = uiState.ledgerOverdueAmount)
	} else if (uiState.orderBlockingDays != null) {
		Text(
			text = stringResource(R.string.s_days_ordering_blocked, uiState.orderBlockingDays),
			modifier = Modifier
				.background(Error90)
				.padding(horizontal = 20.dp, vertical = 12.dp),
			style = textSemiBold14Sp(Color.White)
		)
	}
	WidgetInvoiceList(uiState.invoiceList, ledgerColors, isDCFinanced, detailPageNavigationCallback)
}

@Composable
private fun ILBlockOrderingWidget(amountInRupees: String?) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.background(Error100)
			.height(IntrinsicSize.Min)
	) {
		Column(
			Modifier
				.weight(1f)
				.padding(horizontal = 16.dp, vertical = 12.dp)
		) {
			Text(
				text = stringResource(R.string.ordering_blocked),
				style = textMedium14Sp(Color.White)
			)
			Text(
				text = stringResource(R.string.ordering_blocked_desc, amountInRupees.orEmpty()),
				style = textMedium12Sp(Color.White)
			)
		}
		Image(
			painter = painterResource(R.drawable.ic_ordering_blocked),
			contentDescription = null,
			modifier = Modifier
				.padding(start = 6.dp)
				.fillMaxHeight(),
			contentScale = ContentScale.FillHeight
		)
	}
}

@Composable
private fun WidgetInvoiceList(
	invoiceList: List<WidgetInvoiceViewData>,
	ledgerColors: LedgerColors,
	isDCFinanced: Boolean,
	detailPageNavigationCallback: DetailPageNavigationCallback
) = LazyColumn {
	items(invoiceList) {
		WidgetInvoiceItem(
			data = it, ledgerColors = ledgerColors
		) { invoice ->
			if (isDCFinanced) {
				detailPageNavigationCallback.navigateToRevampInvoiceDetailPage(
					RevampInvoiceDetailViewModel.getBundle(
						ledgerId = invoice.ledgerId, source = invoice.source, erpId = invoice.erpId
					)
				)
			} else {
				detailPageNavigationCallback.navigateToRevampInvoiceDetailPage(
					InvoiceDetailViewModel.getArgs(invoice)
				)
			}
		}
	}
}

@Composable
private fun WidgetInvoiceItem(
	ledgerColors: LedgerColors,
	data: WidgetInvoiceViewData,
	onClick: (data: WidgetInvoiceViewData) -> Unit
) {
	Row(modifier = Modifier
		.clickable { onClick(data) }
		.padding(bottom = 8.dp)
		.fillMaxWidth()
		.wrapContentHeight()
		.background(color = Color.White)
		.padding(top = 16.dp, start = 16.dp, end = 8.dp, bottom = 18.dp),
		verticalAlignment = Alignment.CenterVertically) {
		Image(
			painter = painterResource(R.drawable.ic_ledger_revamp_invoice),
			contentDescription = "transaction icon"
		)
		Column(
			modifier = Modifier
				.weight(1f)
				.padding(start = 16.dp)
		) {
			Row {
				Text(
					text = stringResource(R.string.remaining_amount, data.totalRemainingAmount),
					style = textMedium14Sp(textColor = ledgerColors.CtaDarkColor),
					modifier = Modifier.padding(end = 4.dp).weight(1f)
				)
				InvoiceStatusView(data.invoiceStatus, data.invoiceStatusVariable)
				HandleInterestStartDate(data.invoiceStatus, data.invoiceStatusVariable)
			}
			Text(
				text = stringResource(R.string.invoice_amount_rs, data.totalInvoiceAmount),
				style = textMedium12Sp(textColor = Neutral80)
			)
		}
	}
}

@Composable
private fun HandleInterestStartDate(invoiceStatus: String, invoiceStatusVariable: String) {
	if (invoiceStatus == InvoiceStatus.INTEREST_START_DATE) {
		Text(
			modifier = Modifier
				.background(color = Color20E3E3E3, mediumShape())
				.border(1.dp, Neutral10, mediumShape())
				.padding(horizontal = 8.dp, vertical = 4.dp),
			text = stringResource(
				id = R.string.interest_start_dates,
				invoiceStatusVariable.toLongOrNull().toDateMonthYear()
			),
			style = textCaptionCP1(Neutral70)
		)
	}
}
