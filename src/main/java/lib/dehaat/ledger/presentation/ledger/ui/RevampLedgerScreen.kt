package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.ledger.bottomsheets.FilterScreen
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OutstandingCalculationHeader
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OutstandingCalculationScreen
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.loanlist.InvoiceListViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.LedgerUIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.transactions.ui.TransactionsScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.LedgerHeaderScreen
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.getNumberOfDays
import lib.dehaat.ledger.resources.Background
import moe.tlaster.nestedscrollview.NestedScrollViewState
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RevampLedgerScreen(
	viewModel: RevampLedgerViewModel,
	ledgerColors: LedgerColors,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	onPayNowClick: () -> Unit,
	onOtherPaymentModeClick: () -> Unit,
	onError: (Exception) -> Unit,
	onBackPress: () -> Unit,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	scaffoldState: ScaffoldState = rememberScaffoldState(),
	scope: CoroutineScope = rememberCoroutineScope(),
	sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
	nestedScrollViewState: NestedScrollViewState = rememberNestedScrollViewState()
) {
	val uiState by viewModel.uiState.collectAsState()
	val transactionUIState by viewModel.transactionUIState.collectAsState()
	var filter: Pair<DaysToFilter, Int?> by remember {
		mutableStateOf(Pair(DaysToFilter.All, null))
	}

	var peekHeight by remember {
		mutableStateOf(0.dp)
	}

	val toggleBottomSheet: (Boolean) -> Unit = { showBottomSheet ->
		scope.launch(Dispatchers.Main) {
			if (showBottomSheet) {
				sheetState.show()
			} else {
				sheetState.hide()
			}
		}
	}

	var isTransactionListHeaderVisible by remember { mutableStateOf(true) }

	LaunchedEffect(Unit) {
		viewModel.selectedDaysToFilterEvent.flowWithLifecycle(
			lifecycleOwner.lifecycle,
			Lifecycle.State.STARTED
		).collect { event ->
			viewModel.getTransactionSummaryFromServer(event)
			filter = Pair(event, event.getNumberOfDays())
		}
	}

	ModalBottomSheetLayout(
		sheetContent = {
			Column(modifier = Modifier.padding(bottom = 1.dp)) {
				transactionUIState.outstandingCalculationUiState?.let { outstandingCalculationUiState ->
					OutstandingCalculationScreen(
						outstandingCalculationUiState = outstandingCalculationUiState
					) {
						toggleBottomSheet(sheetState.isVisible.not())
					}
				}
			}
		},
		sheetElevation = 8.dp,
		sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
		sheetState = sheetState
	) {
		CommonContainer(
			title = if (isTransactionListHeaderVisible) {
				viewModel.dcName
			} else {
				stringResource(id = R.string.all_transactions_list)
			},
			onBackPress = onBackPress,
			scaffoldState = scaffoldState,
			backgroundColor = Background,
			ledgerColors = ledgerColors,
			bottomBar = {
				Surface(elevation = 8.dp) {
					transactionUIState.outstandingCalculationUiState?.let { outstandingCalculationUiState ->
						OutstandingCalculationHeader(
							outstandingCalculationUiState = outstandingCalculationUiState,
							peekHeight = { peekHeight = it }
						) {
							toggleBottomSheet(true)
						}
					}
				}
			}
		) { paddingValues ->
			when (uiState.state) {
				is UIState.SUCCESS -> {
					VerticalNestedScrollView(
						state = nestedScrollViewState,
						header = {
							Column {
								LedgerHeaderScreen(
									summaryViewData = uiState.summaryViewData,
									onPayNowClick = onPayNowClick,
									onTotalOutstandingDetailsClick = {
										toggleBottomSheet(true)
									},
									onShowInvoiceListDetailsClick = {
										detailPageNavigationCallback.navigateToInvoiceListPage(
											InvoiceListViewModel.getBundle(
												uiState.summaryViewData?.minInterestOutstandingDate,
												uiState.summaryViewData?.minOutstandingAmountDue,
												viewModel.partnerId
											)
										)
									},
									onOtherPaymentModeClick = onOtherPaymentModeClick,
									outstandingPaymentValid = viewModel::outstandingPaymentValid
								)
							}
						},
						content = {
							Column(
								modifier = Modifier
									.padding(paddingValues)
									.fillMaxWidth()
							) {
								TransactionsScreen(
									ledgerViewModel = viewModel,
									ledgerColors = ledgerColors,
									onError = onError,
									detailPageNavigationCallback = detailPageNavigationCallback,
									showFilterSheet = viewModel::showFilterBottomSheet
								) {
									isTransactionListHeaderVisible = it
								}
								FilterBottomSheetDialog(
									uiState = uiState,
									getStartEndDate = { viewModel.getSelectedDates(it) },
									hideBottomSheet = { daysToFilter ->
										daysToFilter?.let {
											viewModel.updateFilter(it)
										}
										viewModel.hideFilterBottomSheet()
									}
								)
							}
						}
					)
				}

				is UIState.LOADING -> {
					ShowProgressDialog(ledgerColors) {
						viewModel.updateProgressDialog(false)
					}
				}

				is UIState.ERROR -> {
					NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
				}
			}
		}
	}
}

@Composable
private fun FilterBottomSheetDialog(
	uiState: LedgerUIState,
	getStartEndDate: (DaysToFilter) -> Pair<Long, Long>?,
	hideBottomSheet: (DaysToFilter?) -> Unit
) = AnimatedVisibility(
	visible = uiState.showFilterSheet,
	enter = expandVertically(animationSpec = tween(500)),
	exit = shrinkVertically(animationSpec = tween(500))
) {
	Dialog(
		onDismissRequest = {
			hideBottomSheet(null)
		},
		properties = DialogProperties(
			usePlatformDefaultWidth = false,
		)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.clickable(
					indication = null,
					interactionSource = remember { MutableInteractionSource() }
				) { hideBottomSheet(null) },
			contentAlignment = Alignment.BottomCenter
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.fillMaxWidth()
					.background(
						color = Color.White,
						shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
					)
			) {
				FilterScreen(
					appliedFilter = uiState.appliedFilter,
					onFilterApply = { daysToFilter ->
						hideBottomSheet(daysToFilter)
					},
					getStartEndDate = getStartEndDate,
					stateChange = uiState.showFilterSheet
				) {
					hideBottomSheet(null)
				}
			}
		}
	}
}
