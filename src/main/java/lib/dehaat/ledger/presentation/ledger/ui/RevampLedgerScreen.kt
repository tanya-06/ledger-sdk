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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.dehaat.androidbase.helper.isFalse
import com.dehaat.androidbase.helper.showToast
import com.dehaat.wallet.presentation.ui.components.ftue.WalletActivationBottomSheetComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.LedgerSDK.getWalletFTUEStatus
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.LedgerConstants.IS_WALLET_LEDGER_VIEWED
import lib.dehaat.ledger.presentation.RevampLedgerViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.ledger.bottomsheets.FilterScreen
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OutstandingCalculationHeader
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OutstandingCalculationScreen
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.loanlist.InvoiceListViewModel
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerState
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.DownloadLedgerBottomSheet
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.SnackbarHostContent
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.registerDownloadReceiver
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.unRegisterDownloadReceiver
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.LedgerUIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.transactions.ui.TransactionsScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.LedgerHeaderScreen
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.getNumberOfDays
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.util.closeSheet
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
	getWalletFTUEStatus: (String) -> Boolean,
	setWalletFTUEStatus: (String) -> Unit,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	scaffoldState: ScaffoldState = rememberScaffoldState(),
	scope: CoroutineScope = rememberCoroutineScope(),
	sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
	nestedScrollViewState: NestedScrollViewState = rememberNestedScrollViewState()
) {
	val context = LocalContext.current
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
	val downloadLedgerSheet =
		rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

	LaunchedEffect(Unit) {
		viewModel.selectedDaysToFilterEvent.flowWithLifecycle(
			lifecycleOwner.lifecycle, Lifecycle.State.STARTED
		).collect { event ->
			viewModel.getTransactionSummaryFromServer(event)
			filter = Pair(event, event.getNumberOfDays())
		}
	}

	LaunchedEffect(key1 = Unit) {
		viewModel.downloadStarted.collectLatest {
			downloadLedgerSheet.closeSheet(scope)
			viewModel.updateSnackBarType(SnackBarType.DownloadProgress)
			scaffoldState.snackbarHostState.showSnackbar(
				DownloadLedgerState.PROGRESS, duration = SnackbarDuration.Indefinite
			)
		}
	}

	LaunchedEffect(key1 = Unit) {
		viewModel.uiEvent.collectLatest {
			if (it is UiEvent.ShowSnackbar) {
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

	DownloadLedgerBottomSheet(
		viewModel.updateLedgerStartDate,
		ledgerColors,
		transactionUIState.downloadLedgerState,
		downloadLedgerSheet,
		viewModel::updateSelectedFilter,
		{
			viewModel.downloadFormat = it
			viewModel.downloadLedger()
		},
		viewModel::onMonthYearSelected,
		viewModel::onDateRangeSelected
	) {
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
			CommonContainer(title = if (isTransactionListHeaderVisible) {
				viewModel.dcName
			} else {
				stringResource(id = lib.dehaat.ledger.R.string.all_transactions_list)
			},
				onBackPress = onBackPress,
				scaffoldState = scaffoldState,
				backgroundColor = Background,
				ledgerColors = ledgerColors,
				snackbarHost = {
					DownloadLedgerSnackbarHost(it, uiState, scaffoldState, viewModel)
				},
				bottomBar = {
					Surface(elevation = 8.dp) {
						transactionUIState.outstandingCalculationUiState?.let { outstandingCalculationUiState ->
							OutstandingCalculationHeader(outstandingCalculationUiState = outstandingCalculationUiState,
								peekHeight = { peekHeight = it }) {
								toggleBottomSheet(true)
							}
						}
					}
				}) { paddingValues ->
				ObserveLedgerDownloadState(viewModel, scaffoldState.snackbarHostState)
				when (uiState.state) {
					is UIState.SUCCESS -> {
						VerticalNestedScrollView(state = nestedScrollViewState, header = {
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
						}, content = {
							Column(
								modifier = Modifier
									.padding(paddingValues)
									.fillMaxWidth()
							) {
								TransactionsScreen(
									ledgerViewModel = viewModel,
									ledgerColors = ledgerColors,
									onError = onError,
									downloadLedgerSheet = downloadLedgerSheet,
									detailPageNavigationCallback = detailPageNavigationCallback,
									showFilterSheet = viewModel::showFilterBottomSheet
								) {
									isTransactionListHeaderVisible = it
								}
								FilterBottomSheetDialog(uiState = uiState,
									getStartEndDate = { viewModel.getSelectedDates(it) },
									hideBottomSheet = { daysToFilter ->
										daysToFilter?.let {
											viewModel.updateFilter(it)
										}
										viewModel.hideFilterBottomSheet()
									})
							}
						})
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
	if (!getWalletFTUEStatus(IS_WALLET_LEDGER_VIEWED)) {
		LaunchedEffect(Unit){
			delay(2000)
			viewModel.showWalletFTUEBottomSheet()
		}
		WalletFirstTimeDialog(
			uiState,
			setWalletFTUEStatus,
			getWalletFTUEStatus,
			detailPageNavigationCallback,
			viewModel::hideWalletFTUEBottomSheet,
			viewModel::dismissWalletFTUEBottomSheet
		)
	}
}

@Composable
private fun DownloadLedgerSnackbarHost(
	snackbarHostState: SnackbarHostState,
	uiState: LedgerUIState,
	scaffoldState: ScaffoldState,
	viewModel: RevampLedgerViewModel
) = SnackbarHost(snackbarHostState) {
	Snackbar(
		content = {
			SnackbarHostContent(
				uiState.snackbarType, it, scaffoldState, viewModel::downloadLedger
			)
		},
		backgroundColor = Neutral90,
		modifier = Modifier
			.clip(mediumShape())
			.padding(horizontal = 8.dp, vertical = 8.dp)
	)
}

@Composable
private fun ObserveLedgerDownloadState(
	viewModel: RevampLedgerViewModel, snackbarHostState: SnackbarHostState
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
						DownloadLedgerState.SUCCESS, duration = SnackbarDuration.Indefinite
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
		}, properties = DialogProperties(
			usePlatformDefaultWidth = false,
		)
	) {
		Box(modifier = Modifier
			.fillMaxSize()
			.clickable(
				indication = null,
				interactionSource = remember { MutableInteractionSource() }) { hideBottomSheet(null) },
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
					appliedFilter = uiState.appliedFilter, onFilterApply = { daysToFilter ->
						hideBottomSheet(daysToFilter)
					}, getStartEndDate = getStartEndDate, stateChange = uiState.showFilterSheet
				) {
					hideBottomSheet(null)
				}
			}
		}
	}
}

@Composable
private fun WalletFirstTimeDialog(
	uiState: LedgerUIState,
	setWalletFTUEStatus: (String) -> Unit,
	getWalletFTUEStatus: (String) -> Boolean,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	hideWalletFTUEBottomSheet: () -> Unit,
	dismissWalletFTUEBottomSheet: () -> Unit
) = AnimatedVisibility(
	visible = !getWalletFTUEStatus(IS_WALLET_LEDGER_VIEWED) && uiState.showFirstTimeFTUEDialog && !uiState.dismissFirstTimeFTUEDialog,
	enter = expandVertically(animationSpec = tween(500)),
	exit = shrinkVertically(animationSpec = tween(500))
) {
	fun dismissWallet(){
		hideWalletFTUEBottomSheet()
		setWalletFTUEStatus(IS_WALLET_LEDGER_VIEWED)
		dismissWalletFTUEBottomSheet()
	}
	Dialog(
		onDismissRequest = {
			hideWalletFTUEBottomSheet()
		},
		properties = DialogProperties(
			usePlatformDefaultWidth = false,
		)
	) {
		Box(
			contentAlignment = Alignment.BottomCenter,
			modifier = Modifier
				.fillMaxSize()
		) {
			WalletActivationBottomSheetComponent({
				dismissWallet()
				detailPageNavigationCallback.navigateToWalletLedger(bundleOf())
			}, {
				dismissWallet()
			})
		}
	}
}