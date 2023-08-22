package lib.dehaat.ledger.navigation

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dehaat.wallet.presentation.ui.screens.WalletLedgerScreen
import lib.dehaat.ledger.initializer.callbacks.LedgerCallBack
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.LedgerConstants.REFRESH_HOME_SCREEN
import lib.dehaat.ledger.presentation.LedgerHomeScreenViewModel
import lib.dehaat.ledger.presentation.ledger.abs.ABSDetailScreen
import lib.dehaat.ledger.presentation.ledger.annotations.LedgerFlowType
import lib.dehaat.ledger.presentation.ledger.details.availablecreditlimit.AvailableCreditLimitScreenArgs
import lib.dehaat.ledger.presentation.ledger.details.availablecreditlimit.ui.AvailableCreditLimitDetailsScreen
import lib.dehaat.ledger.presentation.ledger.details.debithold.ui.DebitHoldDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.interest.InterestDetailScreenArgs
import lib.dehaat.ledger.presentation.ledger.details.interest.ui.InterestDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.invoice.RevampInvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.details.invoice.ui.RevampInvoiceDetailScreen
import lib.dehaat.ledger.presentation.ledger.details.loanlist.InvoiceListViewModel
import lib.dehaat.ledger.presentation.ledger.details.loanlist.ui.InvoiceListScreen
import lib.dehaat.ledger.presentation.ledger.details.payments.ui.RevampPaymentDetailScreen
import lib.dehaat.ledger.presentation.ledger.invoicelist.ui.WidgetInvoiceListScreen
import lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.ui.RevampCreditNoteDetailsScreen
import lib.dehaat.ledger.presentation.ledger.ui.LedgerHomeScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.SetComposeResultListener
import lib.dehaat.ledger.util.navBaseComposable
import lib.dehaat.ledger.util.setComposeResult

@Composable
fun LedgerNavigation(
	dcName: String,
	partnerId: String,
	isDCFinanced: Boolean,
	ledgerColors: LedgerColors,
	ledgerCallbacks: LedgerCallBack,
	flowType: String?,
	flowTypeData: List<NamedNavArgument>,
	onDownloadClick: (InvoiceDownloadData) -> Unit,
	finishActivity: () -> Unit,
	openOrderDetailFragment: (String) -> Unit,
	getWalletFTUEStatus: (String) -> Boolean,
	setWalletFTUEStatus: (String) -> Unit,
	getWalletHelpVideoId: () -> String
) {

	val navController = rememberNavController()

	val startDestination = when {
		flowType == LedgerFlowType.INVOICE_LIST -> LedgerRoutes.WidgetInvoiceListScreen.screen
		else -> LedgerRoutes.LedgerHomeScreen.screen
	}

	NavHost(
		navController = navController, startDestination = startDestination
	) {
		navBaseComposable(
			route = LedgerRoutes.LedgerHomeScreen.screen,
			arguments = listOf(navArgument(LedgerConstants.KEY_PARTNER_ID) {
				type = NavType.StringType
				defaultValue = partnerId
			}, navArgument(LedgerConstants.KEY_DC_NAME) {
				type = NavType.StringType
				defaultValue = dcName
			}, navArgument(LedgerConstants.KEY_IS_FINANCED) {
				type = NavType.BoolType
				defaultValue = isDCFinanced
			}),
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			val viewModel = hiltViewModel<LedgerHomeScreenViewModel>()
			val launcher = getResultLauncher { viewModel.getInitialData() }
			navController.SetComposeResultListener<Boolean>(REFRESH_HOME_SCREEN) {
				viewModel.getInitialData()
			}
			LedgerHomeScreen(
				viewModel = viewModel,
				ledgerColors = ledgerColors,
				onBackPress = finishActivity,
				detailPageNavigationCallback = provideDetailPageNavCallBacks(navController),
				onPayNowClick = {
					if (isDCFinanced) {
						ledgerCallbacks.onFinancedDCPayNowClick(launcher)
					} else {
						ledgerCallbacks.onNonFinancedDCPayNowClick()
					}
				},
				setWalletFTUEStatus = setWalletFTUEStatus,
				getWalletFTUEStatus = getWalletFTUEStatus
			)
		}

		navBaseComposable(
			route = LedgerRoutes.WalletLedgerRoute.screen,
			arguments = listOf(navArgument(LedgerConstants.KEY_PARTNER_ID) {
				type = NavType.StringType
				defaultValue = partnerId
			}),
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			WalletLedgerScreen(
				onClick = {
					openOrderDetailFragment(it)
				},
				onBackPress = {
					navController.popBackStack()
				},
				setWalletFTUEStatus = setWalletFTUEStatus,
				getWalletFTUEStatus = getWalletFTUEStatus,
				getWalletHelpVideoId = getWalletHelpVideoId
			)
		}



		navBaseComposable(
			route = LedgerRoutes.InvoiceListScreen.screen,
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			val invoiceListViewModel = hiltViewModel<InvoiceListViewModel>()
			InvoiceListScreen(viewModel = invoiceListViewModel,
				ledgerColors = ledgerColors,
				detailPageNavigationCallback = provideDetailPageNavCallBacks(navController),
				onError = { ledgerCallbacks.exceptionHandler(it) }) {
				navController.popBackStack()
			}
		}

		navBaseComposable(
			route = LedgerRoutes.TotalAvailableCreditLimitScreen.screen,
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			val uiState = it.arguments?.let { args -> AvailableCreditLimitScreenArgs(args) }
			AvailableCreditLimitDetailsScreen(
				uiState = uiState?.getArgs(), ledgerColors = ledgerColors
			) {
				navController.popBackStack()
			}
		}

		navBaseComposable(
			route = LedgerRoutes.RevampLedgerInvoiceDetailScreen.screen,
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			val invoiceDetailViewModel = hiltViewModel<RevampInvoiceDetailViewModel>()
			RevampInvoiceDetailScreen(isDCFinanced = isDCFinanced,
				viewModel = invoiceDetailViewModel,
				ledgerColors = ledgerColors,
				onDownloadInvoiceClick = onDownloadClick,
				onError = { ledgerCallbacks.exceptionHandler(it) }) {
				navController.popBackStack()
			}
		}



		navBaseComposable(
			route = LedgerRoutes.RevampLedgerCreditNoteDetailScreen.screen,
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {

			RevampCreditNoteDetailsScreen(

				ledgerColors = ledgerColors,
				onError = { ledgerCallbacks.exceptionHandler(it) },
			) {
				navController.popBackStack()
			}
		}



		navBaseComposable(
			route = LedgerRoutes.RevampLedgerPaymentDetailScreen.screen,
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {

			RevampPaymentDetailScreen(
				ledgerColors = ledgerColors,
				onError = { ledgerCallbacks.exceptionHandler(it) }) {
				navController.popBackStack()
			}
		}

		navBaseComposable(
			route = LedgerRoutes.RevampLedgerWeeklyInterestDetailScreen.screen,
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			val interestViewData = it.arguments?.let { args -> InterestDetailScreenArgs(args) }
			InterestDetailScreen(
				interestViewData = interestViewData?.getArgs(), ledgerColors = ledgerColors
			) {
				navController.popBackStack()
			}
		}

		navBaseComposable(
			route = LedgerRoutes.ABSDetailScreen.screen,
			arguments = listOf(navArgument(LedgerConstants.KEY_PARTNER_ID) {
				type = NavType.StringType
				defaultValue = partnerId
			}),
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			val prepaidHoldAmount =
				it.arguments?.getDouble(LedgerConstants.KEY_PREPAID_HOLD_AMOUNT, 0.0).orZero()
			ABSDetailScreen(prepaidHoldAmount, ledgerColors = ledgerColors, onBackPress = {
				navController.popBackStack()
			})
		}

		navBaseComposable(
			route = LedgerRoutes.DebitHoldDetailScreen.screen,
			arguments = listOf(navArgument(LedgerConstants.KEY_PARTNER_ID) {
				type = NavType.StringType
				defaultValue = partnerId
			}),
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			DebitHoldDetailScreen(ledgerColors = ledgerColors, onBackPress = {
				navController.popBackStack()
			})
		}

		navBaseComposable(
			route = LedgerRoutes.WidgetInvoiceListScreen.screen,
			arguments = flowTypeData.toMutableList().apply {
				add(navArgument(LedgerConstants.KEY_PARTNER_ID) {
					type = NavType.StringType
					defaultValue = partnerId
				})
			},
			logScreenName = ledgerCallbacks.firebaseScreenLogger
		) {
			val launcher = getResultLauncher {
				navController.setComposeResult(REFRESH_HOME_SCREEN, true)
			}
			WidgetInvoiceListScreen(ledgerColors = ledgerColors,
				isDCFinanced = isDCFinanced,
				detailPageNavigationCallback = provideDetailPageNavCallBacks(navController),
				onPayNowClick = {
					if (isDCFinanced) {
						ledgerCallbacks.onFinancedDCPayNowClick(launcher)
					} else {
						ledgerCallbacks.onNonFinancedDCPayNowClick()
					}
				}) {
				if (flowType == LedgerFlowType.INVOICE_LIST) {
					finishActivity()
				} else {
					navController.popBackStack()
				}
			}
		}
	}

}

@Composable
private fun getResultLauncher(
	onActivityResult: () -> Unit
) = rememberLauncherForActivityResult(
	contract = ActivityResultContracts.StartActivityForResult(),
	onResult = {
		if (it.resultCode == Activity.RESULT_OK) {
			onActivityResult()
		}
	})

fun provideDetailPageNavCallBacks(
	navController: NavHostController
) = object : DetailPageNavigationCallback {


	override fun navigateToInvoiceListPage(args: Bundle) {
		navigateToInvoiceListPage(navController, args)
	}


	override fun navigateToRevampInvoiceDetailPage(args: Bundle) {
		navigateToRevampInvoiceDetailPage(navController, args)
	}

	override fun navigateToRevampCreditNoteDetailPage(args: Bundle) {
		navigateToRevampCreditNoteDetailPage(navController, args)
	}

	override fun navigateToRevampPaymentDetailPage(args: Bundle) {
		navigateToRevampPaymentDetailPage(navController, args)
	}


	override fun navigateToHoldAmountDetailPage(args: Bundle) {
		navigateToHoldAmountDetailPage(navController, args)
	}

	override fun navigateToDebitHoldPaymentDetailPage(args: Bundle) {
		navigateToDebitHoldPaymentDetailPage(navController, args)
	}

	override fun navigateToWalletLedger(args: Bundle) {
		navigateToWalletLedger(
			navController = navController, args = args
		)
	}

	override fun navigateToWidgetInvoiceListScreen(args: Bundle) {
		navigateToWidgetInvoiceListPage(navController, args)
	}
}
