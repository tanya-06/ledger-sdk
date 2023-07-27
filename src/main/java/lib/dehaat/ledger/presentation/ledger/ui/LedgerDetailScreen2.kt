@file:OptIn(ExperimentalPagerApi::class)

package lib.dehaat.ledger.presentation.ledger.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.androidbase.helper.isFalse
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.ledger.bottomsheets.DaysToFilterContent
import lib.dehaat.ledger.presentation.ledger.bottomsheets.LenderOutStandingDetails
import lib.dehaat.ledger.presentation.ledger.bottomsheets.OverAllOutStandingDetails
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.credits.ui.CreditsScreen
import lib.dehaat.ledger.presentation.ledger.credits.ui.component.AvailableCreditLimitInfoForLmsAndNonLmsUseModal
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerState
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.DownloadLedgerBottomSheet
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.SnackbarHostContent
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.registerDownloadReceiver
import lib.dehaat.ledger.presentation.ledger.downloadledger.ui.unRegisterDownloadReceiver
import lib.dehaat.ledger.presentation.ledger.state.BottomSheetType
import lib.dehaat.ledger.presentation.ledger.transactions.LedgerTransactionViewModel
import lib.dehaat.ledger.presentation.ledger.transactions.ui.TransactionsListScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.Header
import lib.dehaat.ledger.presentation.ledger.ui.component.Tabs
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionSummary
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.util.HandleAPIErrors
import lib.dehaat.ledger.util.closeSheet
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun LedgerDetailScreen2(
	viewModel: LedgerDetailViewModel,
	ledgerColors: LedgerColors,
	onBackPress: () -> Unit,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	isLmsActivated: () -> Boolean?,
	onPayNowClick: () -> Unit,
	onError: (Exception) -> Unit,
	onPaymentOptionsClick: () -> Unit
) {
	HandleAPIErrors(viewModel.uiEvent)
	val uiState by viewModel.uiState.collectAsState()
	val scaffoldState = rememberScaffoldState()
	val scope = rememberCoroutineScope()
	val sheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden
	)

	var bottomBarVisibility by rememberSaveable { mutableStateOf(true) }

	val context = LocalContext.current
	val transactionVM: LedgerTransactionViewModel = hiltViewModel()
	val transactionUiState by transactionVM.uiState.collectAsState()
	val downloadLedgerSheet =
		rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

	LaunchedEffect(key1 = Unit) {
		transactionVM.downloadStarted.collectLatest {
			downloadLedgerSheet.closeSheet(scope)
			scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
			transactionVM.updateSnackBarType(SnackBarType.DownloadProgress)
			scaffoldState.snackbarHostState.showSnackbar(
				DownloadLedgerState.PROGRESS, duration = SnackbarDuration.Indefinite
			)
		}
	}

	DisposableEffect(key1 = Unit) {
		registerDownloadReceiver(context)
		onDispose {
			unRegisterDownloadReceiver(context)
		}
	}

	ObserveLedgerDownloadState(transactionVM, scaffoldState.snackbarHostState)

	if (uiState.isFilteringWithRange) {
		RangeFilterDialog(
			ledgerColors = ledgerColors,
			filtered = { startDate, endDate ->
				if (startDate != null && endDate != null) {
					viewModel.updateSelectedFilter(DaysToFilter.CustomDays(startDate, endDate))
					viewModel.getTransactionSummaryFromServer(
						DaysToFilter.CustomDays(
							startDate,
							endDate
						)
					)
				}
				viewModel.showDaysRangeFilterDialog(false)
			}
		)
	}
	if (uiState.showOutstandingDialog) {
		AvailableCreditLimitInfoForLmsAndNonLmsUseModal(
			title = stringResource(R.string.total_outstanding_formula),
			ledgerColors = ledgerColors,
			lmsActivated = isLmsActivated(),
			onOkClick = {
				viewModel.closeOutstandingDialog()
			}
		)
	}
	DownloadLedgerBottomSheet(
		viewModel.updateLedgerStartDate,
		ledgerColors,
		transactionUiState.downloadLedgerState,
		downloadLedgerSheet,
		transactionVM::updateDLSelectedFilter,
		{
			transactionVM.downloadFormat = it
			transactionVM.downloadLedger()
		},
		transactionVM::onMonthYearSelected,
		transactionVM::onDateRangeSelected,
		transactionVM::updateEndDate
	) {
		CommonContainer(
			title = viewModel.dcName,
			onBackPress = onBackPress,
			scaffoldState = scaffoldState,
			ledgerColors = ledgerColors,
			snackbarHostContent = {
				SnackbarHostContent(
					transactionUiState.snackbarType,
					it, scaffoldState, transactionVM::downloadLedger
				)
			},
			bottomBar = {
				if (uiState.creditSummaryViewData?.credit?.externalFinancierSupported == false) {
					AnimatedVisibility(
						visible = bottomBarVisibility,
						enter = expandVertically(),
						exit = shrinkVertically()
					) {
						TransactionSummary(viewModel, ledgerColors)
					}
				}
			}
		) {
			if (uiState.isLoading) {
				ShowProgressDialog(ledgerColors) {
					viewModel.updateProgressDialog(false)
				}
			} else {
				ModalBottomSheetLayout(
					modifier = Modifier.padding(it),
					sheetContent = {
						when (val bottomSheetType = uiState.bottomSheetType) {
							is BottomSheetType.LenderOutStanding -> LenderOutStandingDetails(
								data = bottomSheetType.data,
								ledgerColors = ledgerColors
							)

							is BottomSheetType.OverAllOutStanding ->
								OverAllOutStandingDetails(
									data = bottomSheetType.data,
									ledgerColors = ledgerColors
								)

							is BottomSheetType.DaysFilterTypeSheet -> DaysToFilterContent(
								selectedFilter = bottomSheetType.selectedFilter,
								ledgerColors = ledgerColors,
								onFilterSelected = { daysToFilter ->
									viewModel.updateSelectedFilter(daysToFilter)
									viewModel.getTransactionSummaryFromServer(daysToFilter)
									scope.launch {
										sheetState.animateTo(ModalBottomSheetValue.Hidden)
									}
								}
							)
						}

					},
					sheetBackgroundColor = Color.White,
					sheetState = sheetState,
					sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
				) {
					val nestedScrollViewState = rememberNestedScrollViewState()
					VerticalNestedScrollView(
						state = nestedScrollViewState,
						header = {
							if (!uiState.isError) {
								Header(
									creditSummaryData = uiState.creditSummaryViewData,
									ledgerColors = ledgerColors,
									isLmsActivated = isLmsActivated,
									onPayNowClick = onPayNowClick,
									onClickTotalOutstandingInfo = {
										scope.launch {
											if (isLmsActivated() == true) {
												viewModel.openOutstandingDialog()
											} else {
												viewModel.openAllOutstandingModal()
												sheetState.animateTo(ModalBottomSheetValue.Expanded)
											}
										}
									},
									onPaymentOptionsClick = onPaymentOptionsClick
								)
							}
						},
						content = {
							val pagerState = rememberPagerState()
							LaunchedEffect(key1 = pagerState) {
								snapshotFlow { pagerState.currentPage }.collect { currentPage ->
									bottomBarVisibility = currentPage == 0
								}
							}
							Column(modifier = Modifier.background(ledgerColors.TransactionAndCreditScreenBGColor)) {
								Tabs(pagerState, ledgerColors) { currentPage ->
									scope.launch {
										pagerState.animateScrollToPage(page = currentPage)
									}
								}
								HorizontalPager(
									modifier = Modifier
										.weight(1f)
										.background(color = ledgerColors.TransactionAndCreditScreenBGColor),
									state = pagerState,
									verticalAlignment = Alignment.Top,
									count = 2
								) { index ->
									when (index) {
										1 -> CreditsScreen(
											ledgerDetailViewModel = viewModel,
											ledgerColors = ledgerColors,
											isLmsActivated = isLmsActivated,
										) {
											viewModel.showLenderOutstandingModal(it)
											scope.launch {
												sheetState.animateTo(
													ModalBottomSheetValue.Expanded
												)
											}
										}

										else -> TransactionsListScreen(
											ledgerColors = ledgerColors,
											detailPageNavigationCallback = detailPageNavigationCallback,
											ledgerDetailViewModel = viewModel,
											openDaysFilter = {
												viewModel.showDaysFilterBottomSheet()
												scope.launch {
													sheetState.animateTo(
														ModalBottomSheetValue.Expanded
													)
												}
											},
											openRangeFilter = {
												viewModel.showDaysRangeFilterDialog(true)
											},
											downloadLedgerSheet = downloadLedgerSheet,
											onError = onError,
											isLmsActivated = isLmsActivated
										)
									}
								}
							}
						}
					)
				}
			}
		}
	}
}

@Composable
private fun ObserveLedgerDownloadState(
	viewModel: LedgerTransactionViewModel,
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
