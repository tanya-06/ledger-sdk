package lib.dehaat.ledger.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController

fun <T> NavHostController.setComposeArgument(key: String, value: T) =
	currentBackStackEntry?.savedStateHandle?.set(key, value)

fun <T> NavHostController.getComposeArgument(key: String) =
	previousBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> NavHostController.removeComposeArgument(key: String) =
	previousBackStackEntry?.savedStateHandle?.remove<T>(key)

fun <T> NavHostController.getOnceComposeArgument(key: String): T? {
	val value = getComposeArgument<T>(key)
	removeComposeArgument<T>(key)
	return value
}

fun <T> NavHostController.setComposeResult(key: String, value: T) {
	previousBackStackEntry?.savedStateHandle?.set(key, value)
	removeComposeArgument<T>(key)
}

@Composable
fun <T> NavHostController.SetComposeResultListener(key: String, result: (T) -> Unit) {
	val lifecycleOwner = LocalLifecycleOwner.current
	DisposableEffect(lifecycleOwner) {
		val resultListenerLiveData = currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
		val observer: (T) -> Unit = { result(it) }
		resultListenerLiveData?.observe(lifecycleOwner, observer)
		onDispose { resultListenerLiveData?.removeObserver(observer) }
	}
}

fun <T> NavHostController.setDataInPreviousBackStackEntry(key: String, value: T) {
	previousBackStackEntry?.savedStateHandle?.set(key, value)
}

@Composable
fun <T> NavHostController.readComposeResultListenerWithSideEffect(
	key: String,
	result: (T) -> Unit
) {
	val data = currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.observeAsState()
	DisposableEffect(data) {
		data?.value?.let { result.invoke(it) }
		currentBackStackEntry?.savedStateHandle?.remove<T>(key)
		onDispose {
			currentBackStackEntry?.savedStateHandle?.remove<T>(key)
		}
	}
}

@Composable
fun <T> NavHostController.observeComposeResultWithComposeLifecycle(
	key: String,
	result: (T) -> Unit
) {
	val lifecycleOwner = LocalLifecycleOwner.current
	val lifecycle = lifecycleOwner.lifecycle
	if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
		return
	}

	val observer: LifecycleEventObserver = object : LifecycleEventObserver {
		override fun onStateChanged(
			source: LifecycleOwner,
			event: Lifecycle.Event
		) {
			when (event) {
				Lifecycle.Event.ON_START -> {
					val storedResult = currentBackStackEntry?.savedStateHandle?.get<T>(key)
					if (storedResult != null) {
						result.invoke(storedResult)
						currentBackStackEntry?.savedStateHandle?.remove<T>(key)
					}
				}
				Lifecycle.Event.ON_DESTROY -> {
					lifecycle.removeObserver(this)
					currentBackStackEntry?.savedStateHandle?.remove<T>(key)
				}
				else -> Unit
			}
		}
	}
	lifecycle.addObserver(observer)
}


