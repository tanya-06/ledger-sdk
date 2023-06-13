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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.annotations.ILBottomBarType
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceListFlowType
import lib.dehaat.ledger.presentation.ledger.invoicelist.WidgetInvoiceListVM
import lib.dehaat.ledger.resources.Secondary110
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.textMedium12Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.util.formatAmount

@Composable
fun InterestWidget(
	amount: Double,
	onClick: (Bundle) -> Unit,
	date: String,
	lineStart: MutableState<Float>
) = Column(modifier = Modifier.fillMaxWidth()) {
	Column(Modifier.align(Alignment.End)) {
		Row(
			Modifier
				.padding(end = 16.dp, top = 14.dp)
				.fillMaxWidth(0.85f)
				.clip(mediumShape())
				.background(Secondary110)
				.clickable {
					onClick(
						WidgetInvoiceListVM.getArgs(
							InvoiceListFlowType.INTEREST, amount, date,
							ILBottomBarType.INTEREST_STARTED
						)
					)
				}
				.padding(horizontal = 16.dp, vertical = 12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			val formatAmount = remember(amount) { amount.toString().formatAmount() }
			Column(Modifier.weight(1f)) {
				Text(
					text = stringResource(R.string.interest_applied),
					style = textMedium14Sp(Color.White)
				)
				Text(
					text = stringResource(R.string.interest_desc, formatAmount),
					style = textMedium12Sp(Color.White)
				)
			}
			Image(
				painter = painterResource(R.drawable.ic_arrow_right_white2),
				contentDescription = null,
				modifier = Modifier.padding(start = 6.dp)
			)

		}
	}
	LineWithCircle(Secondary110, lineStart)
}