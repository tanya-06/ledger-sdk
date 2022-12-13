package lib.dehaat.ledger.util.tooltip

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import lib.dehaat.ledger.util.tooltip.model.ArrowPosition

class TooltipShape(
	private val cornerRadius: Float,
	private val arrowPosition: ArrowPosition
) : Shape {
	override fun createOutline(
		size: Size,
		layoutDirection: LayoutDirection,
		density: Density
	) = Outline.Generic(
		Path().apply {
			val doubleCornerRadius = cornerRadius * 2
			reset()
			moveTo(cornerRadius, 0f)

			// TOP
			when (arrowPosition) {
				is ArrowPosition.TopRight -> {
					lineTo(
						size.width - cornerRadius - arrowPosition.translationFromRight - arrowPosition.width,
						0f
					)
					lineTo(
						size.width - cornerRadius - arrowPosition.translationFromRight - (arrowPosition.width / 2),
						-1 * arrowPosition.arrowHeight
					)
					lineTo(size.width - cornerRadius - arrowPosition.translationFromRight, 0f)
					lineTo(size.width - cornerRadius, 0f)
				}
				else -> {
					lineTo(size.width - (doubleCornerRadius), 0F)
				}
			}

			arcTo(
				rect = Rect(
					offset = Offset(size.width - cornerRadius, y = 0f),
					size = Size(cornerRadius, cornerRadius)
				),
				startAngleDegrees = 270F,
				sweepAngleDegrees = 90F,
				forceMoveTo = false
			)

			// RIGHT
			lineTo(size.width, size.height - cornerRadius)
			arcTo(
				rect = Rect(
					offset = Offset(size.width - cornerRadius, size.height - cornerRadius),
					size = Size(cornerRadius, cornerRadius)
				),
				startAngleDegrees = 0F,
				sweepAngleDegrees = 90F,
				forceMoveTo = false
			)

			// BOTTOM
			when (arrowPosition) {
				is ArrowPosition.BottomCenter -> {
					lineTo((size.width / 2), size.height)
					lineTo(
						(size.width / 2) - (arrowPosition.width / 2),
						size.height + arrowPosition.height
					)
					lineTo((size.width / 2) - arrowPosition.width, size.height)
					lineTo(cornerRadius, size.height)
				}
				is ArrowPosition.BottomRight -> {
					lineTo(
						size.width - cornerRadius - arrowPosition.translationFromRight,
						size.height
					)
					lineTo(
						size.width - cornerRadius - arrowPosition.translationFromRight - (arrowPosition.arrowWidth / 2),
						size.height + arrowPosition.height
					)
					lineTo(
						size.width - cornerRadius - arrowPosition.translationFromRight - arrowPosition.arrowWidth,
						size.height
					)
					lineTo(cornerRadius, size.height)
				}
				else -> lineTo(cornerRadius, size.height)
			}
			arcTo(
				rect = Rect(
					offset = Offset(0f, size.height - cornerRadius),
					size = Size(cornerRadius, cornerRadius)
				),
				startAngleDegrees = 90F,
				sweepAngleDegrees = 90F,
				forceMoveTo = false
			)

			//LEFT
			lineTo(0F, cornerRadius)
			arcTo(
				rect = Rect(
					offset = Offset(0F, 0F),
					size = Size(cornerRadius, cornerRadius)
				),
				startAngleDegrees = 180F,
				sweepAngleDegrees = 90F,
				forceMoveTo = false
			)
			close()
		}
	)
}
