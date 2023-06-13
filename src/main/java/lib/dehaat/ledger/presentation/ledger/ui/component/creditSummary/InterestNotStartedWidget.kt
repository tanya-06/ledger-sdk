package lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.annotations.ILBottomBarType
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceListFlowType
import lib.dehaat.ledger.presentation.ledger.invoicelist.WidgetInvoiceListVM
import lib.dehaat.ledger.resources.Secondary10
import lib.dehaat.ledger.resources.Secondary110
import lib.dehaat.ledger.resources.Secondary120
import lib.dehaat.ledger.resources.Secondary20
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.textMedium12Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun InterestNotStartedWidget(
	amount: Double, onClick: (Bundle) -> Unit, date: String,
	lineStart: MutableState<Float>
) = Column(Modifier.fillMaxWidth()) {
	Column(Modifier.align(Alignment.End)) {
		Row(
			Modifier
				.padding(end = 16.dp, top = 14.dp)
				.fillMaxWidth(0.85f)
				.clip(mediumShape())
				.background(Secondary10)
				.border(1.dp, Secondary20, mediumShape())
				.clickable {
					onClick(
						WidgetInvoiceListVM.getArgs(
							InvoiceListFlowType.INTEREST, amount, date,
							ILBottomBarType.INTEREST_WILL_START
						)
					)
				}
				.padding(horizontal = 16.dp, vertical = 12.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			val amountInRupees = remember(amount) { amount.toString().getAmountInRupees() }
			Column(Modifier.weight(1f)) {
				Text(
					text = stringResource(R.string.save_from_interest),
					style = textMedium14Sp(Secondary120)
				)
				Text(
					text = getSaveInterestDesc(amountInRupees, date),
					style = textMedium12Sp(Secondary120)
				)
			}
			Image(
				painter = painterResource(R.drawable.ic_arrow_right_white2),
				contentDescription = null,
				modifier = Modifier.padding(start = 6.dp),
				colorFilter = ColorFilter.tint(Secondary120)
			)

		}
	}
	LineWithCircle(Secondary110, lineStart)
}

@Composable
fun getSaveInterestDesc(amount: String, date: String) = buildAnnotatedString {
	val boldString = listOf(amount, date)
	val completeString = stringResource(R.string.save_interest_desc, date, amount)
	append(completeString)
	boldString.forEach {
		val start = completeString.indexOf(it)
		addStyle(SpanStyle(fontWeight = FontWeight.SemiBold), start, start + it.length)
	}

}