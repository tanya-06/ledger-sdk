package lib.dehaat.ledger.navigation

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import lib.dehaat.ledger.initializer.callbacks.LedgerCallBack
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.creditnote.CreditNoteDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.creditnote.ui.CreditNoteDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.invoice.InvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.ui.InvoiceDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.payments.ui.PaymentDetailScreen
import lib.dehaat.ledger.presentation.ledger.ui.LedgerDetailScreen2
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.util.withArgsPath

@Composable
fun LedgerNavigation(
    dcName: String,
    partnerId: String,
    ledgerColors: LedgerColors,
    ledgerCallbacks: LedgerCallBack,
    resultLauncher: ActivityResultLauncher<Intent?>,
    viewModel: LedgerDetailViewModel,
    onDownloadClick: (InvoiceDownloadData) -> Unit,
    finishActivity: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LedgerRoutes.LedgerDetailScreen.screen.withArgsPath(
            LedgerConstants.KEY_PARTNER_ID
        )
    ) {
        composable(
            route = LedgerRoutes.LedgerDetailScreen.screen.withArgsPath(
                LedgerConstants.KEY_PARTNER_ID
            ),
            arguments = listOf(navArgument(LedgerConstants.KEY_PARTNER_ID) {
                type = NavType.StringType
                defaultValue = partnerId
            })
        ) {
            viewModel.dcName = dcName
            LedgerDetailScreen2(
                viewModel = viewModel,
                ledgerColors = ledgerColors,
                onBackPress = finishActivity,
                detailPageNavigationCallback = provideDetailPageNavCallBacks(navController = navController),
                isLmsActivated = {
                    viewModel.isLMSActivated()
                },
                onPayNowClick = {
                    ledgerCallbacks.onClickPayNow(viewModel.uiState.value.creditSummaryViewData)
                },
                onPaymentOptionsClick = {
                    ledgerCallbacks.onPaymentOptionsClick(
                        viewModel.uiState.value.creditSummaryViewData,
                        resultLauncher
                    )
                }
            )
        }

        composable(
            route = LedgerRoutes.LedgerInvoiceDetailScreen.screen
        ) {
            val invoiceDetailViewModel = hiltViewModel<InvoiceDetailViewModel>()
            invoiceDetailViewModel.setIsLmsActivated(viewModel.isLMSActivated())

            InvoiceDetailScreen(
                viewModel = invoiceDetailViewModel,
                ledgerColors = ledgerColors,
                onBackPress = {
                    navController.popBackStack()
                },
                onDownloadInvoiceClick = onDownloadClick
            )
        }

        composable(
            route = LedgerRoutes.LedgerCreditNoteDetailScreen.screen
        ) {
            val creditNoteDetailViewModel = hiltViewModel<CreditNoteDetailViewModel>()

            CreditNoteDetailScreen(
                viewModel = creditNoteDetailViewModel,
                ledgerColors = ledgerColors
            ) {
                navController.popBackStack()
            }

        }

        composable(
            route = LedgerRoutes.LedgerPaymentDetailScreen.screen
        ) {
            val paymentDetailViewModel = hiltViewModel<PaymentDetailViewModel>()
            paymentDetailViewModel.setIsLmsActivated(viewModel.isLMSActivated())

            PaymentDetailScreen(
                viewModel = paymentDetailViewModel,
                ledgerColors = ledgerColors
            ) {
                navController.popBackStack()
            }
        }
    }
}

fun provideDetailPageNavCallBacks(
    navController: NavHostController
) = object : DetailPageNavigationCallback {

    override fun navigateToInvoiceDetailPage(args: Bundle) {
        navigateToInvoiceDetailScreen(
            navController = navController,
            args = args
        )
    }

    override fun navigateToCreditNoteDetailPage(args: Bundle) {
        navigateToCreditNoteDetailScreen(
            navController = navController,
            args = args
        )
    }

    override fun navigateToPaymentDetailPage(args: Bundle) {
        navigateToPaymentDetailScreen(
            navController = navController,
            args = args
        )
    }
}
