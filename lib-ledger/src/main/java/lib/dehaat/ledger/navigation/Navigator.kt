package lib.dehaat.ledger.navigation

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import lib.dehaat.ledger.util.withArgs

fun navigateToInvoiceDetailScreen(
    navController: NavHostController,
    ledgerId: String,
    erpId: String?,
    locusId: String?,
    source: String
) = navController.navigate(
    LedgerRoutes.LedgerInvoiceDetailScreen.screen.withArgs(
        ledgerId,
        erpId,
        locusId,
        source
    )
)

fun navigateToCreditNoteDetailScreen(
    navController: NavHostController,
    ledgerId: String,
    erpId: String?,
    locusId: String?
) = navController.navigate(
    LedgerRoutes.LedgerCreditNoteDetailScreen.screen.withArgs(
        ledgerId,
        erpId,
        locusId
    )
)

fun navigateToPaymentDetailScreen(
    navController: NavHostController,
    ledgerId: String,
    erpId: String?,
    locusId: String?,
    mode: String?
) = navController.navigate(
    LedgerRoutes.LedgerPaymentDetailScreen.screen.withArgs(
        ledgerId,
        erpId,
        locusId,
        mode
    )
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
