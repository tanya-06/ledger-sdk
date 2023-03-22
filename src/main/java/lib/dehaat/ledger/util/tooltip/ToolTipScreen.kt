package lib.dehaat.ledger.util.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.resources.ColorBF4B0A
import lib.dehaat.ledger.resources.textParagraphT2

@Composable
fun ToolTipScreen(
	modifier: Modifier,
	offset: ToolTipOffSet,
	title: String,
	shape: TooltipShape? = null,
	density: Density = LocalDensity.current,
	onClick: () -> Unit
) = Box(
	modifier = modifier
		.offset(
			x = with(density) { offset.x.toDp() },
			y = with(density) { offset.y.toDp() }
		)
) {
	Row(
		modifier = Modifier
			.widthIn(max = 312.dp)
			.background(
				shape = shape ?: RoundedCornerShape(8.dp),
				color = ColorBF4B0A
			)
			.padding(vertical = 12.dp, horizontal = 12.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			modifier = Modifier
				.weight(1f),
			text = title,
			style = textParagraphT2(Color.White)
		)

		HorizontalSpacer(width = 12.dp)

		Icon(
			modifier = Modifier
				.clickable(onClick = onClick),
			painter = painterResource(id = R.drawable.ic_cancel),
			tint = Color.White,
			contentDescription = ""
		)
	}
}

data class ViewOffset(var x: Float = 0F, var y: Float = 0F)

data class ToolTipOffSet(var x: Float = 0F, var y: Float = 0F)
