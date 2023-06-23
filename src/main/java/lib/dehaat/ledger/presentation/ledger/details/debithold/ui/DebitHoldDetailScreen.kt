package lib.dehaat.ledger.presentation.ledger.details.debithold.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.debithold.DebitHoldDetailViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.HandleAPIErrors

@Composable
fun DebitHoldDetailScreen(
	ledgerColors: LedgerColors,
	onBackPress: () -> Unit,
	viewModel: DebitHoldDetailViewModel = hiltViewModel(),
) {
	HandleAPIErrors(viewModel.uiEvent)
	val uiState by viewModel.uiState.collectAsState()
	val debitHoldDetail = uiState.debitHoldDetailViewData

	CommonContainer(
		title = stringResource(id = R.string.hold_amount_details),
		onBackPress = onBackPress,
		ledgerColors = ledgerColors,
		backgroundColor = Neutral10
	) {
		when {
			uiState.isLoading -> {
				ShowProgressDialog(ledgerColors) {
					viewModel.updateProgressDialog(false)
				}
			}

			uiState.state is UIState.ERROR -> {
				NoDataFound {}
			}

			else -> {
				debitHoldDetail?.let { HoldAmountDetailScreen(it) }
			}
		}
	}
}