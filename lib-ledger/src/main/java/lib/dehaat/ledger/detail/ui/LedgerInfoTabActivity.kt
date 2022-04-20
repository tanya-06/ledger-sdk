package lib.dehaat.ledger.detail.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lib.dehaat.ledger.core.theme.*
import me.onebone.toolbar.*

class LedgerInfoTabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LedgerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DummyViewStrategy()
                }
            }
        }
    }
}

val tabData = listOf(
    "MUSIC" to Icons.Filled.Home,
    "MARKET" to Icons.Filled.ShoppingCart
)


@OptIn(ExperimentalPagerApi::class)
@Composable
fun CollapsingToolbarScope.Header(
    pagerState: PagerState,
    tabData: List<Pair<String, ImageVector>>,
    state: CollapsingToolbarScaffoldState
) {

    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column(
        Modifier
            .parallax(1f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Green200,
                        WHITE
                    )
                )
            )
            .fillMaxWidth()
    ) {

        HeaderTotalOutstanding(outstandingAmount = "10,00,000")
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun CollapsingToolbarScope.Tabs(
    tabIndex: Int,
    tabData: List<Pair<String, ImageVector>>,
    coroutineScope: CoroutineScope,
    pagerState: PagerState
) {
    TabRow(
        selectedTabIndex = tabIndex,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[tabIndex])
            )
        },
        modifier = Modifier.road(
            whenCollapsed = Alignment.CenterStart,
            whenExpanded = Alignment.BottomEnd
        )
    ) {
        tabData.forEachIndexed { index, pair ->
            Tab(selected = tabIndex == index, onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }, text = {
                Text(text = pair.first)
            }, icon = {
                Icon(imageVector = pair.second, contentDescription = null)
            })
        }
    }
}

@Preview
@Composable
fun InfoOrderBlockPayImmediate() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(Red200)
            .padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
        text = "Your ordering is blocked. Pay immediately",
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
}


@Preview
@Composable
fun HeaderTotalOutstanding(outstandingAmount: String = "") {
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(text = "Total Outstanding")
        Text(text = "₹ $outstandingAmount")
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Gray,
                )
            ) {
                append("Includes overdue amount of ")
            }

            withStyle(
                style = SpanStyle(
                    color = Red200,
                )
            ) {
                append("₹ 2,00,000")
            }
        })
        Text(
            text = "Overdue limit exhausted",
            color = Red200,
            modifier = Modifier
                .padding(5.dp)
                .background(color = Red100)
        )

        Divider(
            modifier = Modifier.padding(top = 12.dp),
            thickness = 1.dp
        )

    }
}

@Composable
fun BottomPurchasePaymentAmount() {
    Row(
        Modifier
            .background(color = Green500)
            .height(100.dp)
            .fillMaxWidth()
    ) {}
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPagerState(
    @IntRange(from = 0) pageCount: Int,
    @IntRange(from = 0) initialPage: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
    @IntRange(from = 1) initialOffscreenLimit: Int = 1,
    infiniteLoop: Boolean = false
): PagerState = rememberSaveable(saver = PagerState.Saver) {
    PagerState(
        pageCount = pageCount,
        currentPage = initialPage,
        currentPageOffset = initialPageOffset,
        offscreenLimit = initialOffscreenLimit,
        infiniteLoop = infiniteLoop
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LedgerPages(pagerState: PagerState, tabData: List<Pair<String, ImageVector>>) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { index ->

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(1000) { pos ->
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Tab ${tabData[index].first} position $pos"
                )
            }
        }

    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DummyViewStrategy() {
    val state = rememberCollapsingToolbarScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 1,
        infiniteLoop = false,
        initialPage = 0,
    )

    CollapsingToolbarScaffold(
        state = state,
        modifier = Modifier.fillMaxSize(),
        toolbar = {
            Box {}
            Header(pagerState, tabData, state)
        },
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed
    ) {

        LedgerPages(pagerState, tabData)
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                )
            },
            modifier = Modifier
        ) {
            tabData.forEachIndexed { index, pair ->
                Tab(selected = pagerState.currentPage == index, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, text = {
                    Text(text = pair.first)
                }, icon = {
                    Icon(imageVector = pair.second, contentDescription = null)
                })
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun AlignmentStrategy() {
    val state = rememberCollapsingToolbarScaffoldState()
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 1,
        infiniteLoop = false,
        initialPage = 0,
    )

    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth()
                    .background(Color.Blue)
            )

            TabRow(
                selectedTabIndex = tabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[tabIndex])
                    )
                },
                modifier = Modifier.road(
                    whenCollapsed = Alignment.BottomEnd,
                    whenExpanded = Alignment.BottomEnd
                )
            ) {
                tabData.forEachIndexed { index, pair ->
                    Tab(selected = tabIndex == index, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = {
                        Text(text = pair.first)
                    }, icon = {
                        Icon(imageVector = pair.second, contentDescription = null)
                    })
                }
            }

        }
    ) {

        LedgerPages(pagerState = pagerState, tabData = tabData)
    }
}