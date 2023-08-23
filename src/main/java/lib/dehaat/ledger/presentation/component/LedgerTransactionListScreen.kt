@file:OptIn(ExperimentalFoundationApi::class)

package lib.dehaat.ledger.presentation.component

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.dehaat.wallet.presentation.ui.components.EntryPointButtonContent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerTransactionsViewModel
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.ledger.details.debithold.DebitHoldDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.RevampInvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.CreditNoteDetailsViewModel
import lib.dehaat.ledger.presentation.ledger.state.LedgerTransactions
import lib.dehaat.ledger.presentation.ledger.state.TransactionViewData
import lib.dehaat.ledger.presentation.ledger.ui.component.AbsTransactionHeader
import lib.dehaat.ledger.presentation.ledger.ui.component.MonthlyDivider
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionCard
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionFilteringHeader
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionListHeader
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType
import lib.dehaat.ledger.presentation.ledger.ui.component.WeeklyInterestHeader
import lib.dehaat.ledger.presentation.ledger.ui.component.onClickType
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.model.revamp.transactions.TransactionViewDataV2
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldAmountViewData
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.Secondary10
import lib.dehaat.ledger.resources.Secondary20
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.FillMaxWidthColumn
import lib.dehaat.ledger.util.clickableWithCorners
import lib.dehaat.ledger.util.showToast

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LedgerTransactionListScreen(
	ledgerColors: LedgerColors,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	selectedFilterEvent: SharedFlow<DaysToFilter>,
	holdAmountViewData: HoldAmountViewData?,
	isLMSActivated: Boolean,
	onFilterClick: onClickType,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
	viewModel: LedgerTransactionsViewModel = hiltViewModel(),
	context: Context = LocalContext.current,
	state: LazyListState = rememberLazyListState(),
	downloadLedgerSheet: ModalBottomSheetState,
) = FillMaxWidthColumn {
	val scope = rememberCoroutineScope()
	val transactions = viewModel.transactionsList.collectAsLazyPagingItems()
	val filtersUiState by viewModel.filterUiState.collectAsState()
	val uiState by viewModel.uiState.collectAsState()
	val walletBalance = uiState.walletBalance
	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { granted ->
			if (granted) {
				scope.launch { downloadLedgerSheet.show() }
			} else {
				showToast(context, R.string.external_storage_permission_required)
			}
		}
	)
	LazyColumn(modifier = Modifier.fillMaxWidth(), state = state) {

		item {
			if (!LedgerSDK.isWalletActive) {
				AbsTransactionHeader(holdAmountViewData, viewModel.ledgerAnalytics) {
					detailPageNavigationCallback.navigateToHoldAmountDetailPage(it)
				}
			} else {
				HoldAndWalletBalanceContent(
					holdAmountViewData,
					detailPageNavigationCallback,
					viewModel,
					walletBalance
				)
			}
		}

		item {
			TransactionListHeader {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					scope.launch { downloadLedgerSheet.show() }
				} else {
					launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				}
			}
		}

		stickyHeader {
			TransactionFilteringHeader(
				filters = filtersUiState.appliedFilter,
				onFilterClick = onFilterClick,
				getSelectedDates = { daysToFilter ->
					viewModel.getSelectedDates(daysToFilter)
				}
			)
		}

		item {
			WeeklyInterestHeader(filtersUiState.showWeeklyInterestDecreasingLabel)
		}

		itemsIndexed(transactions) { index: Int, transaction: LedgerTransactions? ->
			when (transaction) {
				is LedgerTransactions.Invoice -> TransactionCard(
					transactionType = TransactionType.Invoice(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2(),
					modifier = Modifier.clickableWithCorners {
						transaction.transactionsViewData.erpId?.let { erpId ->
							detailPageNavigationCallback.navigateToRevampInvoiceDetailPage(
								RevampInvoiceDetailViewModel.getBundle(
									ledgerId = transaction.transactionsViewData.ledgerId,
									source = transaction.transactionsViewData.source,
									erpId = erpId
								)
							)
						}
					}
				)

				is LedgerTransactions.CreditNote -> TransactionCard(
					transactionType = TransactionType.CreditNote(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2(),
					modifier = Modifier.clickableWithCorners {
						detailPageNavigationCallback.navigateToRevampCreditNoteDetailPage(
							CreditNoteDetailsViewModel.getBundle(transaction.transactionsViewData.ledgerId)
						)
					}
				)

				is LedgerTransactions.Payment -> TransactionCard(
					transactionType = TransactionType.Payment(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2(),
					modifier = Modifier.clickableWithCorners {
						detailPageNavigationCallback.navigateToRevampPaymentDetailPage(
							PaymentDetailViewModel.getBundle(
								ledgerId = transaction.transactionsViewData.ledgerId,
								unrealizedPayment = transaction.transactionsViewData.unrealizedPayment,
								isLMSActivated = isLMSActivated
							)
						)
					}
				)

				is LedgerTransactions.Interest -> TransactionCard(
					transactionType = TransactionType.Interest(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2()
				)

				is LedgerTransactions.FinancingFee -> TransactionCard(
					transactionType = TransactionType.FinancingFee(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2()
				)

				is LedgerTransactions.DebitNote -> TransactionCard(
					transactionType = TransactionType.DebitNote(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2()
				)

				is LedgerTransactions.DebitEntry -> TransactionCard(
					transactionType = TransactionType.DebitEntry(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2()
				)

				is LedgerTransactions.MonthSeparator -> MonthlyDivider(transaction.transactionsViewData.fromDate)

				is LedgerTransactions.DebitHold -> TransactionCard(
					transactionType = TransactionType.DebitHold(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2(),
					modifier = Modifier.clickable {
						detailPageNavigationCallback.navigateToDebitHoldPaymentDetailPage(
							DebitHoldDetailViewModel.getDebitHoldArgs(transaction.transactionsViewData.ledgerId)
						)
					}
				)

				is LedgerTransactions.ReleasePayment -> TransactionCard(
					transactionType = TransactionType.ReleasePayment(),
					transaction = transaction.transactionsViewData.toTransactionViewDataV2(),
					modifier = Modifier.clickable {})

				else -> Unit
			}
			if (showDivider(
					index,
					transaction,
					transactions
				)
			) {
				Divider(
					modifier = Modifier
						.fillMaxWidth()
						.background(Color.White)
						.padding(horizontal = 20.dp)
				)
			}
		}

		transactions.apply {
			when {
				loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
					item { ShowProgress(ledgerColors) }
				}

				loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount == 0 -> {
					item { NoDataFound {} }
				}
			}
		}
	}

	LaunchedEffect(Unit) {
		selectedFilterEvent.flowWithLifecycle(
			lifecycleOwner.lifecycle,
			Lifecycle.State.STARTED
		).collect { daysToFilter ->
			viewModel.applyDaysFilter(daysToFilter)
		}
	}

}

@Composable
private fun HoldAndWalletBalanceContent(
	abs: HoldAmountViewData?,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	ledgerViewModel: LedgerTransactionsViewModel,
	walletBalance: String
) {
	Divider(modifier = Modifier.background(color = Neutral10).height(16.dp))
	Row(
		modifier = Modifier
			.fillMaxWidth(1f)
			.background(Color.White)
			.height(IntrinsicSize.Min)
			.padding(16.dp)
	) {
		abs?.let {
			EntryPointButtonContent(modifier = Modifier.fillMaxHeight()
				.weight(.5f)
				.padding(start = 8.dp, end = 4.dp),
				headingText = stringResource(id = R.string.hold_balance),
				valueText = it.formattedTotalHoldBalance,
				drawableIcon = R.drawable.ic_lock_white_bg,
				bgColor = Secondary10,
				outlineColor = Secondary20,
				onClick = {
					detailPageNavigationCallback.navigateToHoldAmountDetailPage(
						bundleOf(
							LedgerConstants.ABS_AMOUNT to it.absViewData.absHoldBalance.orZero(),
							LedgerConstants.KEY_PREPAID_HOLD_AMOUNT to it.prepaidHoldAmount.orZero(),
						)
					)
				})
			LaunchedEffect(Unit) {
				ledgerViewModel.ledgerAnalytics.onHoldAmountWidgetViewed()
			}
		}
		EntryPointButtonContent(modifier = Modifier.fillMaxHeight()
			.weight(.5f)
			.padding(start = 8.dp, end = 4.dp),
			valueText = walletBalance,
			onClick = {
				detailPageNavigationCallback.navigateToWalletLedger(bundleOf())
			})
	}
	Divider(modifier = Modifier.background(color = Neutral10).height(16.dp))
}

private fun showDivider(
	index: Int,
	transaction: LedgerTransactions?,
	transactions: LazyPagingItems<LedgerTransactions>
) = when {
	transaction is LedgerTransactions.MonthSeparator -> false
	index != 0 && index != transactions.itemCount - 1 -> {
		transactions[index] !is LedgerTransactions.MonthSeparator && transactions[index + 1] !is LedgerTransactions.MonthSeparator
	}

	else -> false
}

private fun TransactionViewData.toTransactionViewDataV2() = TransactionViewDataV2(
	amount = amount,
	creditNoteReason = creditNoteReason,
	date = date,
	erpId = erpId,
	interestEndDate = interestEndDate,
	interestStartDate = interestStartDate,
	ledgerId = ledgerId,
	locusId = locusId,
	partnerId = partnerId,
	paymentMode = paymentMode,
	source = source,
	sourceNo = sourceNo,
	type = type,
	unrealizedPayment = unrealizedPayment,
	fromDate = fromDate,
	toDate = toDate,
	adjustmentAmount = adjustmentAmount,
	schemeName = schemeName,
	creditAmount = creditAmount,
	prepaidAmount = prepaidAmount,
	invoiceStatus = invoiceStatus,
	statusVariable = statusVariable,
	totalInvoiceAmount = totalInvoiceAmount,
	totalInterestCharged = totalInterestCharged,
	totalRemainingAmount = totalRemainingAmount
)
