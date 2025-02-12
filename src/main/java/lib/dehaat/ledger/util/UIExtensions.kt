package lib.dehaat.ledger.util

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.dehaat.androidbase.helper.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.callbacks.FirebaseScreenLogger
import lib.dehaat.ledger.presentation.common.UiEvent
import kotlin.math.roundToInt

@Composable
fun HandleAPIErrors(
	uiEvent: SharedFlow<UiEvent>
) {
	val errorMessage = stringResource(id = com.agridroid.baselib.R.string.tech_prob)
	val lifecycleOwner = LocalLifecycleOwner.current
	val context = LocalContext.current
	LaunchedEffect(Unit) {
		uiEvent.flowWithLifecycle(
			lifecycleOwner.lifecycle,
			Lifecycle.State.STARTED
		).collect { event ->
			when (event) {
				is UiEvent.ShowSnackBar -> {
					if (LedgerSDK.isDebug) {
						context.showToast(event.message)
					} else {
						context.showToast(errorMessage)
					}
					LedgerSDK.currentApp.ledgerCallBack.exceptionHandler(Exception(event.message))
				}

				else -> Unit
			}
		}
	}
}

fun Modifier.clickableWithCorners(
	borderSize: Dp,
	backgroundColor: Color = Color.White,
	onClick: () -> Unit
) = this
	.background(shape = RoundedCornerShape(borderSize), color = backgroundColor)
	.clip(RoundedCornerShape(borderSize))
	.clickable(onClick = onClick)

fun Modifier.clickableWithCorners(
	percent: Int = 25,
	onClick: () -> Unit
) = this
	.clip(RoundedCornerShape(percent))
	.clickable(onClick = onClick)


fun NavGraphBuilder.navBaseComposable(
	route: String,
	arguments: List<NamedNavArgument> = emptyList(),
	deepLinks: List<NavDeepLink> = emptyList(),
	logScreenName: FirebaseScreenLogger,
	content: @Composable (NavBackStackEntry) -> Unit
) = composable(route, arguments, deepLinks) {
	logScreenName(LocalContext.current, route)
	content(it)
}

data class DottedShape(
	val step: Dp,
) : Shape {
	override fun createOutline(
		size: Size,
		layoutDirection: LayoutDirection,
		density: Density
	) = Outline.Generic(Path().apply {
		val stepPx = with(density) { step.toPx() }
		val stepsCount = (size.width / stepPx).roundToInt()
		val actualStep = size.width / stepsCount
		val dotSize = Size(width = actualStep / 2, height = size.height)
		for (i in 0 until stepsCount) {
			addRect(
				Rect(
					offset = Offset(x = i * actualStep, y = 0f),
					size = dotSize
				)
			)
		}
		close()
	})
}

@Composable
fun GifImage(
	modifier: Modifier,
	@DrawableRes drawable: Int,
	contentDescription: String,
	context: Context = LocalContext.current
) {
	val imageLoader = ImageLoader.Builder(context)
		.components {
			if (SDK_INT >= 28) {
				add(ImageDecoderDecoder.Factory())
			} else {
				add(GifDecoder.Factory())
			}
		}
		.build()
	Image(
		painter = rememberAsyncImagePainter(
			ImageRequest.Builder(context).data(data = drawable).apply(block = {}).build(),
			imageLoader = imageLoader
		),
		contentDescription = contentDescription,
		modifier = modifier.fillMaxWidth(),
	)
}

internal fun ModalBottomSheetState.openSheet(scope: CoroutineScope) {
	scope.launch { this@openSheet.show() }
}

internal fun ModalBottomSheetState.closeSheet(scope: CoroutineScope) {
	scope.launch { this@closeSheet.hide() }
}

@Composable
inline fun FillMaxWidthColumn(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) = Column(modifier.fillMaxWidth()) {
	content()
}

fun ModalBottomSheetState.toggleBottomSheet(
	scope: CoroutineScope,
	showBottomSheet: Boolean,
) = scope.launch(Dispatchers.Unconfined) {
	if (showBottomSheet) {
		show()
	} else {
		hide()
	}
}

suspend fun showSnackBar(
	scaffoldState: ScaffoldState,
	message: String,
) = scaffoldState.snackbarHostState.showSnackbar(message)

fun showToast(
	context: Context,
	message: String,
	length: Int = Toast.LENGTH_LONG
) = Toast.makeText(context, message, length).show()

fun showToast(
	context: Context,
	@StringRes message: Int,
	length: Int = Toast.LENGTH_LONG
) = Toast.makeText(context, context.getString(message), length).show()

@Composable
fun ShowToast(
	message: String,
	length: Int = Toast.LENGTH_LONG,
	context: Context = LocalContext.current
) = showToast(context, message, length)
