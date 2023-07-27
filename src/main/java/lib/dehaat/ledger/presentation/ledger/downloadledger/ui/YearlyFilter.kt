package lib.dehaat.ledger.presentation.ledger.downloadledger.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFilter
import lib.dehaat.ledger.presentation.ledger.downloadledger.state.DownloadLedgerUIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.DownloadLedgerState
import lib.dehaat.ledger.presentation.model.downloadledger.DateRange
import lib.dehaat.ledger.resources.ColorB3B3B3
import lib.dehaat.ledger.resources.ColorECF5FF
import lib.dehaat.ledger.resources.ColorF0F8FF
import lib.dehaat.ledger.resources.Neutral70
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Primary100
import lib.dehaat.ledger.resources.PrimaryGreen
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.smallShape
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.text14Sp
import lib.dehaat.ledger.resources.textMedium14Sp
import lib.dehaat.ledger.resources.textSemiBold14Sp
import lib.dehaat.ledger.util.getFinancialMonthStart
import lib.dehaat.ledger.util.getMonthYear

@Composable
fun YearlyFilter(
	downloadLedgerState: DownloadLedgerState,
	uiState: DownloadLedgerUIState,
	updateSelectedFilter: (Int) -> Unit,
	showDateRangeList: () -> Unit,
	onDateRangeClick: (DateRange, Int) -> Unit,
) = Row(Modifier.padding(top = 24.dp)) {
	val isSelected = downloadLedgerState.selectedDownloadFilter == DownloadLedgerFilter.YEARLY
	RadioButton(
		selected = isSelected,
		onClick = { updateSelectedFilter(DownloadLedgerFilter.YEARLY) },
		colors = RadioButtonDefaults.colors(selectedColor = Primary100)
	)
	Column {
		Text(
			text = stringResource(R.string.previous_year_statement),
			style = textSemiBold14Sp(if (isSelected) Neutral90 else Neutral70)
		)
		if (uiState.selectedDateRangeIndex != -1) {
			Row(
				Modifier
					.padding(top = 4.dp)
					.fillMaxWidth()
					.clip(mediumShape())
					.border(1.dp, if (isSelected) Neutral70 else ColorB3B3B3, mediumShape())
					.clickable(isSelected) { showDateRangeList() }
					.padding(8.dp),
				verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = stringResource(
						R.string.fy_s,
						uiState.yearsRangeList[uiState.selectedDateRangeIndex].displayDateRange
					),
					style = textMedium14Sp(if (isSelected) Neutral70 else ColorB3B3B3),
					modifier = Modifier.weight(1f)
				)
				Image(
					painter = painterResource(if (uiState.showYearRangeList) R.drawable.ic_up else R.drawable.ic_down),
					contentDescription = null,
					colorFilter = ColorFilter.tint(if (isSelected) Neutral80 else ColorB3B3B3)
				)
			}
			YearNote(uiState, isSelected)
		}
		AnimatedVisibility(uiState.showYearRangeList) {
			YearRangeList(uiState, onDateRangeClick)
		}
	}
}

@Composable
private fun YearNote(uiState: DownloadLedgerUIState, isSelected: Boolean) {
	val ledgerEndDate = uiState.yearsRangeList[uiState.selectedDateRangeIndex].ledgerEndDate
	if (ledgerEndDate != null) {
		val currentYear = remember { getMonthYear(getFinancialMonthStart()) }
		val endYear = remember { getMonthYear(ledgerEndDate.times(1000)) }
		Text(
			text = stringResource(R.string.ledger_current_year_msg, currentYear, endYear),
			style = text12Sp(if (isSelected) PrimaryGreen else ColorB3B3B3),
			modifier = Modifier.padding(top = 4.dp, start = 4.dp)
		)
	}
}

@Composable
private fun YearRangeList(
	uiState: DownloadLedgerUIState, onDateRangeClick: (DateRange, Int) -> Unit
) = Column(
	modifier = Modifier
		.padding(top = 4.dp)
		.heightIn(0.dp, 124.dp)
		.verticalScroll(rememberScrollState())
		.border(1.dp, ColorECF5FF, smallShape())
) {
	uiState.yearsRangeList.forEachIndexed { index, dateRange ->
		YearRangeItem(index, dateRange, uiState.selectedDateRangeIndex, onDateRangeClick)
	}
}

@Composable
private fun YearRangeItem(
	index: Int,
	dateRange: DateRange,
	selectedDateRangeIndex: Int,
	onItemClick: (DateRange, Int) -> Unit
) = Column {
	val isSelected = selectedDateRangeIndex == index
	Text(text = dateRange.displayDateRange,
		style = text14Sp(Neutral90),
		modifier = Modifier
			.fillMaxWidth()
			.background(if (isSelected) ColorF0F8FF else Color.White)
			.padding(horizontal = 12.dp, vertical = 8.dp)
			.clickable { onItemClick(dateRange, index) })
	Line(color = ColorECF5FF)
}
