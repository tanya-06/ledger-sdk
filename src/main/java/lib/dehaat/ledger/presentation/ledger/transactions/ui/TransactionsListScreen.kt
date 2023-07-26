package lib.dehaat.ledger.presentation.ledger.transactions.ui

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.dehaat.androidbase.helper.showToast
import kotlinx.coroutines.launch
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthName
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.ledger.details.creditnote.CreditNoteDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.debithold.DebitHoldDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.InvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.presentation.ledger.prepaid.HoldAmountWidget
import lib.dehaat.ledger.presentation.ledger.transactions.LedgerTransactionViewModel
import lib.dehaat.ledger.presentation.ledger.transactions.constants.TransactionType
import lib.dehaat.ledger.presentation.ledger.transactions.ui.component.TransactionInvoiceItem
import lib.dehaat.ledger.presentation.ledger.ui.component.FilterStrip
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionCard
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType.DebitEntry
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType.DebitNote
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType.FinancingFee
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType.Interest
import lib.dehaat.ledger.presentation.model.revamp.transactions.TransactionViewDataV2
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.presentation.model.transactions.toStartAndEndDates
import lib.dehaat.ledger.resources.textBold14Sp

@Composable
fun TransactionsListScreen(
	ledgerColors: LedgerColors,
	detailPageNavigationCallback: DetailPageNavigationCallback,
	viewModel: LedgerTransactionViewModel = hiltViewModel(),
	ledgerDetailViewModel: LedgerDetailViewModel,
	openDaysFilter: () -> Unit,
	openRangeFilter: () -> Unit,
	downloadLedgerSheet: ModalBottomSheetState,
	onError: (Exception) -> Unit,
	isLmsActivated: () -> Boolean?
) {
	val uiState by viewModel.uiState.collectAsState()
	val transactions = viewModel.transactionsList.collectAsLazyPagingItems()
	val detailPageState by ledgerDetailViewModel.uiState.collectAsState()
	val filterState = detailPageState.selectedDaysFilter
	val holdAmountData = detailPageState.transactionSummaryViewData?.holdAmountViewData
	val lifecycleOwner = LocalLifecycleOwner.current
	val context = LocalContext.current
	val scope = rememberCoroutineScope()

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { granted ->
			if (granted) {
				scope.launch { downloadLedgerSheet.animateTo(ModalBottomSheetValue.Expanded) }
			} else {
				Toast.makeText(
					context,
					context.getString(R.string.external_storage_permission_required),
					Toast.LENGTH_LONG
				).show()
			}
		}
	)


	Column {
		holdAmountData?.let {
			HoldAmountWidget(holdAmount = holdAmountData, viewModel.ledgerAnalytics) {
				detailPageNavigationCallback.navigateToHoldAmountDetailPage(it)
			}
			Spacer(modifier = Modifier.height(16.dp))
		}

		FilterStrip(
			modifier = Modifier.padding(horizontal = 18.dp),
			ledgerColors = ledgerColors,
			onDaysToFilterIconClick = openDaysFilter,
			onDateRangeFilterIconClick = openRangeFilter,
			onLedgerDownloadClick = {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					scope.launch { downloadLedgerSheet.animateTo(ModalBottomSheetValue.Expanded) }
				} else {
					launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				}

			}
		)

		filterState?.toStartAndEndDates()?.let {
			Text(
				modifier = Modifier.fillMaxWidth(),
				text = stringResource(
					id = R.string.untill,
					it.first.toDateMonthName(),
					it.second.toDateMonthName()
				),
				textAlign = TextAlign.Center,
				style = textBold14Sp()
			)
		}
		LazyColumn(
			modifier = Modifier.fillMaxWidth(),
			contentPadding = PaddingValues(16.dp),
		) {
			items(transactions) { data ->
				data?.let {
					when (it.type) {
						TransactionType.INTEREST -> TransactionCard(
							transactionType = Interest(),
							transaction = it.toTransactionViewDataV2()
						)

						TransactionType.FINANCING_FEE -> TransactionCard(
							transactionType = FinancingFee(),
							transaction = it.toTransactionViewDataV2()
						)

						TransactionType.DEBIT_NOTE -> TransactionCard(
							transactionType = DebitNote(),
							transaction = it.toTransactionViewDataV2()
						)

						TransactionType.DEBIT_ENTRY -> TransactionCard(
							transactionType = DebitEntry(),
							transaction = it.toTransactionViewDataV2()
						)

						else -> TransactionInvoiceItem(
							data = it,
							ledgerColors = ledgerColors
						) { transaction ->
							when (transaction.type) {
								TransactionType.DEBIT_HOLD -> detailPageNavigationCallback.navigateToDebitHoldPaymentDetailPage(
									DebitHoldDetailViewModel.getDebitHoldArgs(it.ledgerId)
								)

								TransactionType.PAYMENT -> detailPageNavigationCallback.navigateToPaymentDetailPage(
									PaymentDetailViewModel.getArgs(transaction)
								)

								TransactionType.CREDIT_NOTE -> detailPageNavigationCallback.navigateToCreditNoteDetailPage(
									CreditNoteDetailViewModel.getArgs(transaction)
								)

								TransactionType.INVOICE -> {
									transaction.erpId?.let {
										detailPageNavigationCallback.navigateToInvoiceDetailPage(
											InvoiceDetailViewModel.getArgs(transaction)
										)
									}
								}

								else -> Unit
							}
						}
					}
					Divider(color = Color.Transparent, thickness = 8.dp)
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
	}
	LaunchedEffect(Unit) {
		viewModel.uiEvent.flowWithLifecycle(
			lifecycleOwner.lifecycle,
			Lifecycle.State.STARTED
		).collect { event ->
			when (event) {
				is UiEvent.ShowSnackbar -> {
					context.showToast(event.message)
				}

				is UiEvent.RefreshList -> {
					transactions.refresh()
				}

				else -> Unit
			}
		}
	}

	LaunchedEffect(Unit) {
		ledgerDetailViewModel.selectedDaysToFilterEvent.flowWithLifecycle(
			lifecycleOwner.lifecycle,
			Lifecycle.State.STARTED
		).collect { event ->
			viewModel.applyDaysFilter(event)
		}
	}
}

private fun TransactionViewData.toTransactionViewDataV2() = with(this) {
	TransactionViewDataV2(
		amount = amount,
		creditNoteReason = creditNoteReason,
		date = date,
		erpId = erpId,
		interestEndDate = interestEndDate,
		interestStartDate = interestStartDate,
		ledgerId = ledgerId,
		locusId = locusId?.toIntOrNull(),
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
	)
}
