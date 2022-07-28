package lib.dehaat.ledger.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.dehaat.androidbase.helper.showToast
import kotlinx.coroutines.flow.SharedFlow
import lib.dehaat.ledger.presentation.common.UiEvent

@Composable
fun HandleAPIErrors(
    uiEvent: SharedFlow<UiEvent>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        uiEvent.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    context.showToast(event.message)
                }
                else -> Unit
            }
        }
    }
}
