package lib.dehaat.ledger.presentation.ledger.downloadledger.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dehaat.androidbase.helper.showToast
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.SelectDateType
import lib.dehaat.ledger.presentation.ledger.downloadledger.state.DownloadLedgerUIState
import lib.dehaat.ledger.presentation.ledger.downloadledger.viewmodels.DownloadLedgerVM
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Primary100
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.textSemiBold16Sp

@Composable
fun ShowDSDatePicker(
	downloadLedgerState: DownloadLedgerState,
	uiState: DownloadLedgerUIState,
	viewModel: DownloadLedgerVM,
	onMonthYearSelected: (Pair<Int, Int>, String) -> Unit
) {
	if (uiState.showDatePicker) {
		Dialog(onDismissRequest = viewModel::dismissDialog) {
			Column(
				Modifier
					.clip(mediumShape())
					.background(Color.White)
					.padding(vertical = 8.dp)
			) {
				Text(
					text = if (uiState.selectedDateType == SelectDateType.FROM) {
						stringResource(R.string.statement_date_colon_from)
					} else {
						stringResource(R.string.statement_date_colon_to)
					},
					modifier = Modifier.padding(top = 8.dp, start = 16.dp),
					style = textSemiBold16Sp(Neutral90)
				)
				Line()
				Row(Modifier.padding(vertical = 24.dp)) {
					NumberPicker(
						state = remember(uiState.selectedMonth) { mutableStateOf(uiState.selectedMonth) },
						numberList = uiState.monthList,
						modifier = Modifier.weight(1f),
						onStateChanged = viewModel::onMonthSelected
					)
					NumberPicker(
						state = remember { mutableStateOf(uiState.yearsList.lastIndex) },
						numberList = uiState.yearsList,
						modifier = Modifier.weight(1f),
						onStateChanged = viewModel::onYearSelected
					)
				}
				Line()
				BottomRow(viewModel, downloadLedgerState, uiState, onMonthYearSelected)
			}
		}
		LaunchedEffect(key1 = Unit) {
			viewModel.initData()
		}
	}
}

@Composable
private fun BottomRow(
	viewModel: DownloadLedgerVM,
	downloadLedgerState: DownloadLedgerState,
	uiState: DownloadLedgerUIState,
	onMonthYearSelected: (Pair<Int, Int>, String) -> Unit
) = Row(
	horizontalArrangement = Arrangement.End,
	modifier = Modifier
		.fillMaxWidth()
		.padding(bottom = 8.dp)
) {
	val context = LocalContext.current
	Text(text = stringResource(R.string.cancel),
		style = textSemiBold16Sp(Primary100),
		modifier = Modifier
			.padding(horizontal = 24.dp, vertical = 10.dp)
			.clickable { viewModel.dismissDialog() })
	Text(text = stringResource(R.string.confirm),
		style = textSemiBold16Sp(Primary100),
		modifier = Modifier
			.padding(horizontal = 24.dp, vertical = 10.dp)
			.clickable {
				if (viewModel.validateMonthYearPair(downloadLedgerState)) {
					onMonthYearSelected(
						viewModel.getMonthYearPair(), uiState.selectedDateType
					)
					viewModel.dismissDialog()
				} else {
					showErrorToast(context, uiState.selectedDateType)
				}
			})
}

private fun showErrorToast(context: Context, selectedDateType: String) {
	context.showToast(context.getString(getMessage(selectedDateType)))
}

private fun getMessage(selectedDateType: String) = when (selectedDateType) {
	SelectDateType.FROM -> R.string.from_cannot_be_greater
	SelectDateType.TO -> R.string.to_cannot_be_lesser
	else -> 0
}