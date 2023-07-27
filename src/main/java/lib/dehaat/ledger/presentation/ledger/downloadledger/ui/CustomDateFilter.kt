package lib.dehaat.ledger.presentation.ledger.downloadledger.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.SelectDateType
import lib.dehaat.ledger.presentation.ledger.downloadledger.state.DownloadLedgerUIState
import lib.dehaat.ledger.presentation.ledger.downloadledger.viewmodels.DownloadLedgerVM
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.resources.ColorB3B3B3
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Primary100
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.resources.textSemiBold14Sp

@Composable
fun CustomDateFilter(
	viewModel: DownloadLedgerVM,
	downloadLedgerState: DownloadLedgerState,
	uiState: DownloadLedgerUIState,
	updateSelectedFilter: (Int) -> Unit
) = Row(Modifier.padding(top = 24.dp)) {
	val isSelected = downloadLedgerState.selectedDownloadFilter == DownloadLedgerFilter.CUSTOM
	RadioButton(
		selected = isSelected,
		onClick = { updateSelectedFilter(DownloadLedgerFilter.CUSTOM) },
		colors = RadioButtonDefaults.colors(selectedColor = Primary100)
	)
	Column {
		Text(
			text = stringResource(R.string.enter_custom_date),
			style = textSemiBold14Sp(if (isSelected) Neutral90 else Neutral70)
		)
		Row(
			Modifier
				.fillMaxWidth()
				.padding(top = 4.dp)
		) {
			FromDateRow(isSelected, viewModel, uiState, downloadLedgerState)
			DashLine(isSelected)
			ToDateRow(isSelected, viewModel, uiState, downloadLedgerState)
		}

	}
}

@Composable
private fun RowScope.DashLine(isSelected: Boolean) = Box(
	modifier = Modifier
		.padding(horizontal = 4.dp)
		.height(2.dp)
		.width(12.dp)
		.background(if (isSelected) Neutral70 else ColorB3B3B3, mediumShape())
		.align(Alignment.CenterVertically)
)

@Composable
private fun RowScope.ToDateRow(
	isSelected: Boolean,
	viewModel: DownloadLedgerVM,
	uiState: DownloadLedgerUIState,
	downloadLedgerState: DownloadLedgerState
) {
	Row(
		Modifier
			.weight(1f)
			.clip(mediumShape())
			.border(1.dp, if (isSelected) Neutral70 else ColorB3B3B3, mediumShape())
			.clickable(isSelected) { viewModel.selectDate(SelectDateType.TO) }
			.padding(8.dp)
	) {
		Text(
			text = if (downloadLedgerState.toDate != null) {
				stringResource(
					R.string.to_s,
					"${uiState.monthList[downloadLedgerState.toDate.first]} ${downloadLedgerState.toDate.second}"
				)
			} else {
				stringResource(R.string.to_date)
			},
			style = textMedium14Sp(if (isSelected) Neutral70 else ColorB3B3B3),
			modifier = Modifier.weight(1f)
		)
		Image(
			painter = painterResource(R.drawable.ic_date_range),
			contentDescription = null,
			colorFilter = ColorFilter.tint(if (isSelected) Neutral70 else ColorB3B3B3)
		)
	}
}

@Composable
private fun RowScope.FromDateRow(
	isSelected: Boolean,
	viewModel: DownloadLedgerVM,
	uiState: DownloadLedgerUIState,
	downloadLedgerState: DownloadLedgerState
) = Row(
	Modifier
		.weight(1f)
		.clip(mediumShape())
		.border(1.dp, ColorB3B3B3, mediumShape())
		.clickable(isSelected) { viewModel.selectDate(SelectDateType.FROM) }
		.padding(8.dp)
) {
	Text(
		text = if (downloadLedgerState.fromDate != null) {
			stringResource(
				R.string.from_s,
				"${uiState.monthList[downloadLedgerState.fromDate.first]} ${downloadLedgerState.fromDate.second}"
			)
		} else {
			stringResource(R.string.from_date)
		},
		style = textMedium14Sp(if (isSelected) Neutral70 else ColorB3B3B3),
		modifier = Modifier.weight(1f)
	)
	Image(
		painter = painterResource(R.drawable.ic_date_range),
		contentDescription = null,
		colorFilter = ColorFilter.tint(if (isSelected) Neutral70 else ColorB3B3B3)
	)
}
