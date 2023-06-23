package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import lib.dehaat.ledger.presentation.ledger.bottomsheets.FilterScreen
import lib.dehaat.ledger.presentation.ledger.state.LedgerFilterUIState
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter

@Composable
fun FilterBottomSheetDialog(
	uiState: LedgerFilterUIState,
	getStartEndDate: (DaysToFilter) -> Pair<Long, Long>?,
	hideBottomSheet: (DaysToFilter?) -> Unit
) = AnimatedVisibility(
	visible = uiState.showFilterSheet,
	enter = expandVertically(animationSpec = tween(500)),
	exit = shrinkVertically(animationSpec = tween(500))
) {
	Dialog(
		onDismissRequest = {
			hideBottomSheet(null)
		},
		properties = DialogProperties(
			usePlatformDefaultWidth = false,
		)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.clickable(
					indication = null,
					interactionSource = remember { MutableInteractionSource() }
				) { hideBottomSheet(null) },
			contentAlignment = Alignment.BottomCenter
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.fillMaxWidth()
					.background(
						color = Color.White,
						shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
					)
			) {
				FilterScreen(
					appliedFilter = uiState.appliedFilter,
					onFilterApply = { daysToFilter ->
						hideBottomSheet(daysToFilter)
					},
					getStartEndDate = getStartEndDate,
					stateChange = uiState.showFilterSheet
				) {
					hideBottomSheet(null)
				}
			}
		}
	}
}
