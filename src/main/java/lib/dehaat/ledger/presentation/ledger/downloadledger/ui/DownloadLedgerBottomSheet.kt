package lib.dehaat.ledger.presentation.ledger.downloadledger.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.common.uicomponent.BaseBottomSheet
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFormat
import lib.dehaat.ledger.presentation.ledger.downloadledger.viewmodels.DownloadLedgerVM
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import lib.dehaat.ledger.presentation.model.downloadledger.DownloadLedgerData
import lib.dehaat.ledger.resources.Color303030
import lib.dehaat.ledger.resources.ColorB3B3B3
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Primary100
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.text18Sp
import lib.dehaat.ledger.resources.textMedium16Sp
import lib.dehaat.ledger.util.closeSheet

@Composable
fun DownloadLedgerBottomSheet(
	updateLedgerStartDateFlow: SharedFlow<DownloadLedgerData>,
	ledgerColors: LedgerColors,
	uiState: DownloadLedgerState,
	downloadLedgerSheet: ModalBottomSheetState,
	updateSelectedFilter: (Int) -> Unit,
	onDownloadLedgerClick: (String) -> Unit,
	onMonthYearSelected: (Pair<Int, Int>, String) -> Unit,
	updateDateRange: (DateRange) -> Unit,
	updateEndDate: (Long?) -> Unit = {},
	content: @Composable () -> Unit
) = BaseBottomSheet(sheetState = downloadLedgerSheet, content = content) {
	Box(Modifier.height(IntrinsicSize.Min)) {
		DownloadLedgerBSContent(
			uiState,
			downloadLedgerSheet,
			updateSelectedFilter,
			onDownloadLedgerClick,
			onMonthYearSelected,
			updateDateRange,
			updateLedgerStartDateFlow,
			updateEndDate
		)
		ShowProgress(ledgerColors, showProgress = uiState.isLoading)
	}
}

@Composable
private fun DownloadLedgerBSContent(
	downloadLedgerState: DownloadLedgerState,
	downloadLedgerSheet: ModalBottomSheetState,
	updateSelectedFilter: (Int) -> Unit,
	onDownloadLedgerClick: (String) -> Unit,
	onMonthYearSelected: (Pair<Int, Int>, String) -> Unit,
	updateDateRange: (DateRange) -> Unit,
	updateLedgerStartDateFlow: SharedFlow<DownloadLedgerData>,
	updateEndDate: (Long?) -> Unit
) = Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
	val viewModel = hiltViewModel<DownloadLedgerVM>()
	val uiState by viewModel.uiState.collectAsState()
	ShowDSDatePicker(downloadLedgerState, uiState, viewModel, onMonthYearSelected)
	DownloadStatement(downloadLedgerSheet)
	Line(Modifier.padding(top = 16.dp))
	Image(
		painter = painterResource(R.drawable.ic_download_ledger),
		contentDescription = null,
		modifier = Modifier
			.align(Alignment.CenterHorizontally)
			.padding(top = 24.dp)
	)
	YearlyFilter(
		downloadLedgerState, uiState, updateSelectedFilter, viewModel::alterDateRangeList
	) { dateRange, selectedIndex ->
		viewModel.alterDateRangeList()
		viewModel.updateSelectedDateRange(selectedIndex)
		updateDateRange(dateRange)
	}
	CustomDateFilter(viewModel, downloadLedgerState, uiState, updateSelectedFilter)
	DownloadButtons(downloadLedgerState.enableDownloadBtn, onDownloadLedgerClick)
	LaunchedEffect(Unit) {
		updateLedgerStartDateFlow.collectLatest {
			updateEndDate(it.ledgerEndDate)
			viewModel.updateYearsList(it.ledgerStartDate.times(1000), it.ledgerEndDate)
		}
	}
}

@Composable
private fun DownloadButtons(
	enableDownloadBtn: Boolean,
	onDownloadLedgerClick: (String) -> Unit
) {
	if (LedgerSDK.isAIMS) {
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
			OutlinedButton(
				modifier = Modifier
					.padding(top = 36.dp)
					.weight(1f),
				onClick = { onDownloadLedgerClick(DownloadLedgerFormat.EXCEL) },
				shape = mediumShape(),
				colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
				border = BorderStroke(1.dp, if (enableDownloadBtn) Primary100 else ColorB3B3B3),
				enabled = enableDownloadBtn
			) {
				Text(
					modifier = Modifier.padding(vertical = 8.dp),
					text = stringResource(id = R.string.download_excel),
					style = textMedium16Sp(Neutral70)
				)
			}
			OutlinedButton(
				modifier = Modifier
					.padding(top = 36.dp)
					.weight(1f),
				onClick = { onDownloadLedgerClick(DownloadLedgerFormat.PDF) },
				shape = mediumShape(),
				colors = ButtonDefaults.buttonColors(backgroundColor = if (enableDownloadBtn) Primary100 else ColorB3B3B3),
				enabled = enableDownloadBtn
			) {
				Text(
					modifier = Modifier.padding(vertical = 8.dp),
					text = stringResource(id = R.string.download_pdf),
					style = textMedium16Sp(Color.White)
				)
			}
		}
	} else {
		OutlinedButton(
			modifier = Modifier
				.padding(top = 36.dp, start = 16.dp)
				.fillMaxWidth(),
			onClick = { onDownloadLedgerClick(DownloadLedgerFormat.PDF) },
			shape = mediumShape(),
			colors = ButtonDefaults.buttonColors(backgroundColor = if (enableDownloadBtn) Primary100 else ColorB3B3B3),
			enabled = enableDownloadBtn
		) {
			Text(
				modifier = Modifier.padding(vertical = 8.dp),
				text = stringResource(id = R.string.download_now),
				style = textMedium16Sp(Color.White)
			)
		}
	}
}

@Composable
private fun DownloadStatement(downloadLedgerSheet: ModalBottomSheetState) =
	Row(verticalAlignment = Alignment.CenterVertically) {
		val scope = rememberCoroutineScope()
		Text(
			text = stringResource(R.string.ledger_download),
			style = text18Sp(fontWeight = FontWeight.Bold, textColor = Color303030),
			modifier = Modifier
				.weight(1f)
				.padding(top = 12.dp)
		)
		Image(
			painter = painterResource(R.drawable.ledger_ic_cancel),
			contentDescription = null,
			modifier = Modifier
				.padding(top = 12.dp)
				.size(24.dp)
				.padding(2.dp)
				.clickable { downloadLedgerSheet.closeSheet(scope) }
		)
	}