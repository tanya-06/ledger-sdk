package lib.dehaat.ledger.navigation

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun navigateToInvoiceListPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.InvoiceListScreen.screen,
    args = args
)

fun navigateToWalletLedger(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.WalletLedgerRoute.screen,
    args = args
)

fun navigateToRevampInvoiceDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.RevampLedgerInvoiceDetailScreen.screen,
    args = args
)

fun navigateToRevampCreditNoteDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.RevampLedgerCreditNoteDetailScreen.screen,
    args = args
)

fun navigateToRevampPaymentDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.RevampLedgerPaymentDetailScreen.screen,
    args = args
)

fun navigateToHoldAmountDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.ABSDetailScreen.screen,
    args = args
)
fun navigateToDebitHoldPaymentDetailPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.DebitHoldDetailScreen.screen,
    args = args
)

fun NavController.navigateTo(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(route).toUri())
        .build()

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) {
        val destination = deepLinkMatch.destination
        val id = destination.id
        navigate(id, args, navOptions, navigatorExtras)
    } else {
        navigate(route, navOptions, navigatorExtras)
    }
}

fun navigateToWidgetInvoiceListPage(
    navController: NavHostController,
    args: Bundle
) = navController.navigateTo(
    route = LedgerRoutes.WidgetInvoiceListScreen.screen,
    args = args
)
