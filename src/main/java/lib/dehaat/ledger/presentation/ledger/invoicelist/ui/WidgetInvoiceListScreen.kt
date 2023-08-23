package lib.dehaat.ledger.presentation.ledger.invoicelist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import lib.dehaat.ledger.R
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.ledger.invoicelist.WidgetInvoiceListVM
import lib.dehaat.ledger.presentation.ledger.invoicelist.ui.components.WidgetILBottomBar
import lib.dehaat.ledger.presentation.ledger.invoicelist.ui.components.WidgetInvoiceListContent
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.HandleAPIErrors

@Composable
fun WidgetInvoiceListScreen(
	viewModel: WidgetInvoiceListVM,
	ledgerColors: LedgerColors,
	isDCFinanced: Boolean,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	onPayNowClick: (String?) -> Unit,
	onBackPress: () -> Unit
) {
	val scaffoldState = rememberScaffoldState()
	val uiState by viewModel.uiState.collectAsState()
	CommonContainer(title = stringResource(R.string.invoice_list),
		onBackPress = onBackPress,
		ledgerColors = ledgerColors,
		scaffoldState = scaffoldState,
		backgroundColor = Background,
		bottomBar = { WidgetILBottomBar(uiState.bottomBarData) { onPayNowClick(uiState.widgetType) } }) {
		Box(Modifier.padding(it)) {
			WidgetInvoiceListContent(
				uiState, ledgerColors, isDCFinanced, detailPageNavigationCallback
			)
			ShowProgress(ledgerColors = ledgerColors, uiState.isLoading)
			HandleAPIErrors(uiEvent = viewModel.uiEvent)
		}
	}
}



