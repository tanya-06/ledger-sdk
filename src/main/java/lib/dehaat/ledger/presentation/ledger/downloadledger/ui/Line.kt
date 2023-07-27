package lib.dehaat.ledger.presentation.ledger.downloadledger.ui

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.resources.Neutral10

@Composable
fun Line(modifier: Modifier = Modifier, color: Color = Neutral10) =
	Divider(modifier = modifier, color = Neutral10, thickness = 1.dp)
