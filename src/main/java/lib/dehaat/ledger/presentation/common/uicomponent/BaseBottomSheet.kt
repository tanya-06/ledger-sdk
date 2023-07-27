package lib.dehaat.ledger.presentation.common.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun BaseBottomSheet(
	sheetState: ModalBottomSheetState,
	content: @Composable () -> Unit,
	sheetShape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
	sheetContent: @Composable () -> Unit,
) = ModalBottomSheetLayout(
	modifier = Modifier,
	sheetContent = { Column(Modifier.padding(bottom = 1.dp)) { sheetContent() } },
	sheetBackgroundColor = Color.White,
	sheetState = sheetState,
	sheetShape = sheetShape,
	content = content
)
