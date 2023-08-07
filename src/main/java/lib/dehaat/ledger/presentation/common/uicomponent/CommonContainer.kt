package lib.dehaat.ledger.presentation.common.uicomponent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import lib.dehaat.ledger.initializer.themes.LedgerColors

@Composable
fun CommonContainer(
	modifier: Modifier = Modifier,
	title: String,
	onBackPress: () -> Unit,
	ledgerColors: LedgerColors,
	scaffoldState: ScaffoldState = rememberScaffoldState(),
	backgroundColor: Color = Color.White,
	bottomBar: @Composable () -> Unit = {},
	snackbarHost: @Composable (SnackbarHostState) -> Unit = {
		SnackbarHost(it) {
			Snackbar(content = {
				Text(it.message)
			})
		}
	},
	content: @Composable (PaddingValues) -> Unit
) {
	Scaffold(
		modifier = modifier,
		scaffoldState = scaffoldState,
		snackbarHost = snackbarHost,
		topBar = {
			CustomAppBar(
				title,
				onBackPress = onBackPress,
				ledgerColors = ledgerColors
			)
		},
		backgroundColor = backgroundColor,
		bottomBar = bottomBar,
		content = content
	)
}
