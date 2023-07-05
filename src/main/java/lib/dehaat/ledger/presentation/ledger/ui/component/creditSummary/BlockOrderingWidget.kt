package lib.dehaat.ledger.presentation.ledger.ui.component.creditSummary

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dehaat.androidbase.helper.pxToDp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.annotations.ILBottomBarType
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceListFlowType
import lib.dehaat.ledger.presentation.ledger.invoicelist.WidgetInvoiceListVM
import lib.dehaat.ledger.resources.Error100
import lib.dehaat.ledger.resources.textMedium12Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.util.getAmountInRupees

@Composable
fun BlockOrderingWidget(amount: Double, date: String, onClick: (Bundle) -> Unit) =
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.background(Error100)
			.height(IntrinsicSize.Min)
			.clickable {
				onClick(
					WidgetInvoiceListVM.getArgs(
						InvoiceListFlowType.OVERDUE,
						amount, date, ILBottomBarType.ORDERING_BLOCKED
					)
				)
			}
	) {
		val amountInRupees = remember(amount) { amount.toString().getAmountInRupees() }
		Column(
			Modifier
				.weight(1f)
				.padding(horizontal = 16.dp, vertical = 12.dp)) {
			Text(
				text = stringResource(R.string.ordering_blocked),
				style = textMedium14Sp(Color.White)
			)
			Text(
				text = stringResource(R.string.ordering_blocked_desc, amountInRupees),
				style = textMedium12Sp(Color.White)
			)
		}
		Image(
			painter = painterResource(R.drawable.ic_arrow_large),
			contentDescription = null,
			modifier = Modifier.padding(start = 6.dp)
		)
		Image(
			painter = painterResource(R.drawable.ic_ordering_blocked),
			contentDescription = null,
			modifier = Modifier
				.padding(start = 6.dp)
				.fillMaxHeight(),
			contentScale = ContentScale.FillHeight
		)
	}

@Composable
fun LineWithCircle(color: Color = Color.Black, lineStart: MutableState<Float>) {
	val xOffset: Dp by remember { derivedStateOf { (lineStart.value).pxToDp().dp } }
	Column(
		modifier = Modifier
			.offset(x = xOffset),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(
			modifier = Modifier
				.height(18.dp)
				.width(2.dp)
				.background(color)
		)
		Box(
			modifier = Modifier
				.size(8.dp)
				.background(color, CircleShape)
		)
	}
}
