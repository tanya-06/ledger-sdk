package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.androidbase.helper.isFalse
import com.dehaat.androidbase.helper.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.LedgerHomeScreenViewModel
import lib.dehaat.ledger.presentation.LedgerTransactionsViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.CommonModalBottomSheetLayout
import lib.dehaat.ledger.presentation.component.LedgerHeaderScreen
import lib.dehaat.ledger.presentation.component.LedgerTransactionListScreen
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OutstandingCalculationHeader
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OutstandingCalculationScreen
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerState
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.DownloadLedgerBottomSheet
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.SnackbarHostContent
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.registerDownloadReceiver
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.unRegisterDownloadReceiver
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.closeSheet
import lib.dehaat.ledger.util.showSnackBar
import lib.dehaat.ledger.util.toggleBottomSheet
import moe.tlaster.nestedscrollview.NestedScrollViewState
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun LedgerHomeScreen(
	viewModel: LedgerHomeScreenViewModel,
	ledgerColors: LedgerColors,
	onBackPress: () -> Unit,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	onPayNowClick: () -> Unit,
	scaffoldState: ScaffoldState = rememberScaffoldState(),
	scope: CoroutineScope = rememberCoroutineScope(),
	sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
	nestedScrollViewState: NestedScrollViewState = rememberNestedScrollViewState()
) {
	val uiState by viewModel.uiState.collectAsState()
	val ledgerFilterUIState by viewModel.filterUiState.collectAsState()
	val context = LocalContext.current
	val transactionVM: LedgerTransactionsViewModel = hiltViewModel()
	val transactionUIState by transactionVM.uiState.collectAsState()
	val downloadLedgerSheet =
		rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

	LaunchedEffect(key1 = Unit) {
		transactionVM.downloadStarted.collectLatest {
			downloadLedgerSheet.closeSheet(scope)
			transactionVM.updateSnackBarType(SnackBarType.DownloadProgress)
			scaffoldState.snackbarHostState.showSnackbar(
				DownloadLedgerState.PROGRESS, duration = SnackbarDuration.Indefinite
			)
		}
	}

	LaunchedEffect(key1 = Unit) {
		viewModel.uiEvent.collectLatest {
			if (it is UiEvent.ShowSnackBar) {
				context.showToast(it.message)
			}
		}
	}

	DisposableEffect(key1 = Unit) {
		registerDownloadReceiver(context)
		onDispose {
			unRegisterDownloadReceiver(context)
		}

	}
	ObserveLedgerDownloadState(transactionVM, scaffoldState.snackbarHostState)

	DownloadLedgerBottomSheet(
		viewModel.updateLedgerStartDate,
		ledgerColors,
		transactionUIState.downloadLedgerState,
		downloadLedgerSheet,
		transactionVM::updateSelectedFilter,
		{
			transactionVM.downloadFormat = it
			transactionVM.downloadLedger()
		},
		transactionVM::onMonthYearSelected,
		transactionVM::onDateRangeSelected,
		transactionVM::updateEndDate
	) {
		CommonModalBottomSheetLayout(
			sheetState = sheetState,
			sheetContent = {
				uiState.outstandingCalculationUiState?.let { outstandingCalculationUiState ->
					OutstandingCalculationScreen(
						outstandingCalculationUiState = outstandingCalculationUiState,
						isFinancedDc = viewModel.isFinancedDc,
						showBottomSheet = {
							sheetState.toggleBottomSheet(
								scope = scope,
								showBottomSheet = false
							)
						}
					)
				}
			}
		) {
			CommonContainer(
				title = viewModel.dcName,
				onBackPress = onBackPress,
				scaffoldState = scaffoldState,
				ledgerColors = ledgerColors,
				snackbarHostContent = {
					SnackbarHostContent(
						transactionUIState.snackbarType,
						it,
						scaffoldState,
						transactionVM::downloadLedger
					)
				},
				bottomBar = {
					Surface(elevation = 8.dp) {
						uiState.ledgerTotalCalculation?.let {
							OutstandingCalculationHeader(
								totalPurchase = it.totalPurchase,
								totalPayment = it.totalPayment
							) {
								sheetState.toggleBottomSheet(
									scope = scope,
									showBottomSheet = true
								)
							}
						}
					}
				}
			) {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(it)
				) {
					when (uiState.state) {
						UIState.SUCCESS -> {
							VerticalNestedScrollView(
								state = nestedScrollViewState,
								header = {
									LedgerHeaderScreen(
										totalOutstandingAmount = uiState.outstandingAmount,
										onTotalOutstandingClick = {
											sheetState.toggleBottomSheet(
												scope = scope,
												showBottomSheet = true
											)
										},
										onPayNowClick = onPayNowClick
									)
								},
								content = {
									LedgerTransactionListScreen(
										ledgerColors = ledgerColors,
										detailPageNavigationCallback = detailPageNavigationCallback,
										selectedFilterEvent = viewModel.selectedDaysToFilterEvent,
										holdAmountViewData = uiState.holdAmountViewData,
										isLMSActivated = viewModel.isLMSActivated,
										onFilterClick = {
											viewModel.showFilterBottomSheet()
										},
										downloadLedgerSheet = downloadLedgerSheet
									)
									FilterBottomSheetDialog(
										uiState = ledgerFilterUIState,
										getStartEndDate = { viewModel.getSelectedDates(it) }
									) { daysToFilter ->
										daysToFilter?.let {
											viewModel.updateFilter(it)
										}
										viewModel.hideFilterBottomSheet()
									}
								}
							)
						}

						UIState.LOADING -> ShowProgressDialog(ledgerColors = ledgerColors) {}
						is UIState.ERROR -> {

						}
					}
				}
			}
		}
	}

	LaunchedEffect(Unit) {
		viewModel.uiEvent.collectLatest {
			when (it) {
				is UiEvent.ShowSnackBar -> showSnackBar(scaffoldState, it.message)
				UiEvent.RefreshList -> Unit
				UiEvent.Success -> Unit
			}
		}
	}
}

@Composable
private fun ObserveLedgerDownloadState(
	viewModel: LedgerTransactionsViewModel,
	snackbarHostState: SnackbarHostState
) {
	val scope = rememberCoroutineScope()
	val downloadState by LedgerSDK.downloadStatusLiveData.observeAsState()
	downloadState?.consume {
		if (it?.status == true) {
			val (_, filePath, mimeType) = it
			if (filePath != null) {
				snackbarHostState.currentSnackbarData?.dismiss()
				viewModel.updateSnackBarType(
					SnackBarType.DownloadSuccess(filePath, mimeType)
				)
				scope.launch {
					snackbarHostState.showSnackbar(
						DownloadLedgerState.SUCCESS,
						duration = SnackbarDuration.Indefinite
					)
				}
			}
		} else if (it?.status.isFalse()) {
			snackbarHostState.currentSnackbarData?.dismiss()
			viewModel.updateSnackBarType(SnackBarType.DownloadFailed)
			scope.launch {
				snackbarHostState.showSnackbar(
					DownloadLedgerState.FAILED, duration = SnackbarDuration.Indefinite
				)
			}
		}
	}
}

