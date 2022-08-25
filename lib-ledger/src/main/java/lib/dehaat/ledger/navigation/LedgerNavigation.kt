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
            route = LedgerRoutes.LedgerCreditNoteDetailScreen.screen.withArgsPath(
                LedgerConstants.KEY_LEDGER_ID,
                LedgerConstants.KEY_ERP_ID,
                LedgerConstants.KEY_LOCUS_ID,
            ),
            arguments = listOf(
                navArgument(LedgerConstants.KEY_LEDGER_ID) {
                    type = NavType.StringType
                },
                navArgument(LedgerConstants.KEY_ERP_ID) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(LedgerConstants.KEY_LOCUS_ID) {
                    type = NavType.StringType
                    nullable = true
                }
            )
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
            route = LedgerRoutes.LedgerPaymentDetailScreen.screen.withArgsPath(
                LedgerConstants.KEY_LEDGER_ID,
                LedgerConstants.KEY_ERP_ID,
                LedgerConstants.KEY_LOCUS_ID,
                LedgerConstants.KEY_PAYMENT_MODE
            ),
            arguments = listOf(
                navArgument(LedgerConstants.KEY_LEDGER_ID) {
                    type = NavType.StringType
                },
                navArgument(LedgerConstants.KEY_ERP_ID) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(LedgerConstants.KEY_LOCUS_ID) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(LedgerConstants.KEY_PAYMENT_MODE) {
                    type = NavType.StringType
                    nullable = true
                }
            )
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
    override fun navigateToInvoiceDetailPage(
        args: Bundle
    ) {
        navigateToInvoiceDetailScreen(
            navController = navController,
            args = args
        )
    }

    override fun navigateToCreditNoteDetailPage(
        legerId: String,
        erpId: String?,
        locusId: String?
    ) {
        navigateToCreditNoteDetailScreen(
            navController = navController,
            ledgerId = legerId,
            erpId = erpId,
            locusId = locusId
        )
    }

    override fun navigateToPaymentDetailPage(
        legerId: String,
        erpId: String?,
        locusId: String?,
        mode: String?
    ) {
        navigateToPaymentDetailScreen(
            navController = navController,
            ledgerId = legerId,
            erpId = erpId,
            locusId = locusId,
            mode = mode
        )
    }
}
