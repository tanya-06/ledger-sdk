package lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.annotations.ILBottomBarType
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceListFlowType
import lib.dehaat.ledger.presentation.ledger.invoicelist.WidgetInvoiceListVM
import lib.dehaat.ledger.resources.Error110
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.textMedium12Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.resources.textSemiBold14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun OverdueWidget(
	amount: Double, date: String, onClick: (Bundle) -> Unit,
	lineStart: MutableState<Float>
) = Column(Modifier.fillMaxWidth()) {
	Column(Modifier.align(Alignment.End)) {
		Column(
			Modifier
				.padding(end = 16.dp, top = 14.dp)
				.fillMaxWidth(0.7f)
				.clip(mediumShape())
				.background(Error110)
				.clickable {
					onClick(
						WidgetInvoiceListVM.getArgs(
							InvoiceListFlowType.OVERDUE, amount, date,
							ILBottomBarType.ORDERING_WILL_BLOCKED
						)
					)
				}
				.padding(horizontal = 16.dp, vertical = 12.dp)
		) {
			val amountInRupees = remember(amount) { amount.toString().getAmountInRupees() }
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = stringResource(R.string.about_to_overdue),
					style = textMedium14Sp(Color.White),
					modifier = Modifier.weight(1f)
				)
				Text(
					text = amountInRupees,
					style = textSemiBold14Sp(Color.White)
				)
				Image(
					painter = painterResource(R.drawable.ic_arrow_right_white2),
					contentDescription = null,
					modifier = Modifier.padding(start = 6.dp)
				)
			}
			Text(
				text = stringResource(R.string.overdue_desc, date, amountInRupees),
				style = textMedium12Sp(Color.White)
			)
		}
	}
	if (lineStart.value > 0) {
		LineWithCircle(Error110, lineStart)
	}
}


@Preview
@Composable
fun OverdueWidgetPreview() = Column() {
	val lineStart = remember { mutableStateOf(425f) }
	OverdueWidget(20.0, "30 April", {}, lineStart)
}