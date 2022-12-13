package lib.dehaat.ledger.util.tooltip.model

sealed class ArrowPosition(
	val width: Float,
	val height: Float
) {
	data class TopRight(
		val arrowWidth: Float,
		val arrowHeight: Float,
		val translationFromRight: Float
	) : ArrowPosition(arrowWidth, arrowHeight)

	data class BottomCenter(
		val arrowWidth: Float,
		val arrowHeight: Float,
	) : ArrowPosition(arrowWidth, arrowHeight)

	data class BottomRight(
		val arrowWidth: Float,
		val arrowHeight: Float,
		val translationFromRight: Float
	) : ArrowPosition(arrowWidth, arrowHeight)
}
