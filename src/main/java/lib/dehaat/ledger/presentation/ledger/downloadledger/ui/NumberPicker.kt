package lib.dehaat.ledger.presentation.ledger.downloadledger.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lib.dehaat.ledger.resources.Primary10
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun NumberPicker(
	state: MutableState<Int>,
	modifier: Modifier = Modifier,
	numberList: List<String>,
	onStateChanged: (Int) -> Unit = {}
) {
	val range: IntRange = 0..numberList.lastIndex
	val coroutineScope = rememberCoroutineScope()
	val numbersColumnHeight = 36.dp
	val halvedNumbersColumnHeight = numbersColumnHeight / 2
	val halvedNumbersColumnHeightPx =
		with(LocalDensity.current) { halvedNumbersColumnHeight.toPx() }

	fun animatedStateValue(offset: Float): Int =
		state.value - (offset / halvedNumbersColumnHeightPx).toInt()

	val animatedOffset = remember { Animatable(0f) }.apply {
		val offsetRange = remember(state.value, range) {
			val value = state.value
			val first = -(range.last - value) * halvedNumbersColumnHeightPx
			val last = -(range.first - value) * halvedNumbersColumnHeightPx
			first..last
		}
		updateBounds(offsetRange.start, offsetRange.endInclusive)
	}
	val coercedAnimatedOffset = animatedOffset.value % halvedNumbersColumnHeightPx
	val animatedStateValue = animatedStateValue(animatedOffset.value)

	Column(
		modifier = modifier
			.wrapContentSize()
			.draggable(
				orientation = Orientation.Vertical,
				state = rememberDraggableState { deltaY ->
					coroutineScope.launch {
						animatedOffset.snapTo(animatedOffset.value + deltaY)
					}
				},
				onDragStopped = { velocity ->
					coroutineScope.launch {
						val endValue = animatedOffset.fling(initialVelocity = velocity,
							animationSpec = exponentialDecay(frictionMultiplier = 20f),
							adjustTarget = { target ->
								val coercedTarget = target % halvedNumbersColumnHeightPx
								val coercedAnchors = listOf(
									-halvedNumbersColumnHeightPx, 0f, halvedNumbersColumnHeightPx
								)
								val coercedPoint =
									coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
								val base =
									halvedNumbersColumnHeightPx * (target / halvedNumbersColumnHeightPx).toInt()
								coercedPoint + base
							}).endState.value

						state.value = animatedStateValue(endValue)
						onStateChanged(state.value)
						animatedOffset.snapTo(0f)
					}
				})
	) {
		Box {
			FakeColumn()
			NumberColumn(coercedAnimatedOffset, numberList, animatedStateValue)
		}
	}
}

@Composable
fun BoxScope.NumberColumn(
	coercedAnimatedOffset: Float,
	numberList: List<String>,
	animatedStateValue: Int
) = Column(modifier = Modifier
	.align(Alignment.Center)
	.offset { IntOffset(x = 0, y = coercedAnimatedOffset.roundToInt()) }) {
	val baseLabelModifier = Modifier
	val textStyle: TextStyle = LocalTextStyle.current
	ProvideTextStyle(textStyle) {
		Label(
			text = numberList.getOrNull((animatedStateValue - 1)).orEmpty(),
			modifier = baseLabelModifier
				.padding(vertical = 10.dp)
				.fillMaxWidth()
				.alpha(0.5f)
		)
		Label(
			text = numberList.getOrNull(animatedStateValue).orEmpty(),
			modifier = baseLabelModifier
				.padding(vertical = 10.dp)
				.fillMaxWidth()
				.alpha(1f)
		)
		Label(
			text = numberList.getOrNull((animatedStateValue + 1)).orEmpty(),
			modifier = baseLabelModifier
				.padding(vertical = 10.dp)
				.fillMaxWidth()
				.alpha(0.5f),
		)
	}
}

@Composable
fun FakeColumn() {
	val textStyle: TextStyle = LocalTextStyle.current
	Column {
		ProvideTextStyle(textStyle) {
			Text(
				text = "", modifier = Modifier
					.padding(vertical = 10.dp)
					.alpha(0f)
			)
			Text(
				text = "",
				modifier = Modifier
					.fillMaxWidth()
					.background(Primary10)
					.padding(vertical = 10.dp)
					.alpha(1f)
			)
			Text(
				text = "", modifier = Modifier
					.padding(vertical = 10.dp)
					.alpha(0f)
			)
		}
	}
}

@Composable
private fun Label(text: String, modifier: Modifier) {
	Text(
		text = text, modifier = modifier.pointerInput(Unit) {
			detectTapGestures(onLongPress = {
				// Empty to disable text selection
			})
		}, textAlign = TextAlign.Center
	)
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
	initialVelocity: Float,
	animationSpec: DecayAnimationSpec<Float>,
	adjustTarget: ((Float) -> Float)?,
	block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null,
): AnimationResult<Float, AnimationVector1D> {
	val targetValue = animationSpec.calculateTargetValue(value, initialVelocity)
	val adjustedTarget = adjustTarget?.invoke(targetValue)

	return if (adjustedTarget != null) {
		animateTo(
			targetValue = adjustedTarget, initialVelocity = initialVelocity, block = block
		)
	} else {
		animateDecay(
			initialVelocity = initialVelocity,
			animationSpec = animationSpec,
			block = block,
		)
	}
}