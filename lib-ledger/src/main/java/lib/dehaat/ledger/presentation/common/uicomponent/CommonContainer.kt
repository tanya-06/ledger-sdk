package lib.dehaat.ledger.presentation.common.uicomponent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun CommonContainer(
    title: String,
    onBackPress: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    backgroundColor: Color = Color.White,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomAppBar(title, onBackPress = onBackPress)
        },
        backgroundColor = backgroundColor,
        bottomBar = bottomBar,
        content = content
    )
}

