package lib.dehaat.ledger.presentation.common.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.resources.themes.LedgerColors

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

@Composable
fun CommonModalBottomSheetLayout(
	sheetState: ModalBottomSheetState,
	sheetContent: @Composable () -> Unit = {},
	sheetElevation: Dp = 8.dp,
	sheetShape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
	content: @Composable () -> Unit
) = ModalBottomSheetLayout(
	sheetContent = {
		Column(modifier = Modifier.padding(bottom = 0.5.dp)) {
			sheetContent()
		}
	},
	sheetElevation = sheetElevation,
	sheetShape = sheetShape,
	sheetState = sheetState,
	content = content
)
