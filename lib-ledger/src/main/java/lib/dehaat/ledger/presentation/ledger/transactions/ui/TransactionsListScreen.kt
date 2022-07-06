package lib.dehaat.ledger.presentation.ledger.transactions.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.navigation.DetailPageNavigationCallback
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgress
import lib.dehaat.ledger.presentation.ledger.transactions.LedgerTransactionViewModel
import lib.dehaat.ledger.presentation.ledger.transactions.constants.TransactionType
import lib.dehaat.ledger.presentation.ledger.transactions.ui.component.TransactionInvoiceItem
import lib.dehaat.ledger.presentation.ledger.ui.component.FilterStrip

@Composable
fun TransactionsListScreen(
    ledgerColors: LedgerColors,
    detailPageNavigationCallback: DetailPageNavigationCallback,
    viewModel: LedgerTransactionViewModel = hiltViewModel(),
    ledgerDetailViewModel: LedgerDetailViewModel,
    openDaysFilter: () -> Unit,
    openRangeFilter: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val transactions = viewModel.transactionsList.collectAsLazyPagingItems()
    val lifecycleOwner = LocalLifecycleOwner.current

    Column {
        FilterStrip(modifier = Modifier.padding(horizontal = 18.dp),
            ledgerColors = ledgerColors,
            withPenalty = uiState.onlyPenaltyInvoices,
            onWithPenaltyChange = {
                viewModel.applyOnlyPenaltyInvoicesFilter(it)
            },
            onDaysToFilterIconClick = openDaysFilter,
            onDateRangeFilterIconClick = openRangeFilter
        )
        LazyColumn(
            modifier = Modifier,
            contentPadding = PaddingValues(16.dp),
        ) {
            items(transactions) { data ->
                data?.let {
                    TransactionInvoiceItem(data = it, ledgerColors = ledgerColors) {
                        when (data.type) {
                            TransactionType.PAYMENT -> detailPageNavigationCallback.navigateToPaymentDetailPage(
                                legerId = data.ledgerId,
                                erpId = data.erpId,
                                locusId = data.locusId,
                                mode = data.paymentMode
                            )
                            TransactionType.CREDIT_NOTE -> detailPageNavigationCallback.navigateToCreditNoteDetailPage(
                                legerId = data.ledgerId,
                                erpId = data.erpId,
                                locusId = data.locusId,
                            )
                            TransactionType.INVOICE -> detailPageNavigationCallback.navigateToInvoiceDetailPage(
                                legerId = data.ledgerId,
                                erpId = data.erpId,
                                locusId = data.locusId,
                            )
                            else -> Unit
                        }
                    }
                    Divider(color = Color.Transparent, thickness = 8.dp)
                }
            }

            transactions.apply {
                when {
                    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                        item { ShowProgress() }
                    }
                    loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount == 0 -> {
                        item { NoDataFound() }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                    }
                    is UiEvent.RefreshList -> {
                        transactions.refresh()
                    }
                    else -> Unit
                }
            }
    }

    LaunchedEffect(Unit) {
        ledgerDetailViewModel.selectedDaysToFilterEvent.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { event ->
                viewModel.applyDaysFilter(event)
            }
    }
}
