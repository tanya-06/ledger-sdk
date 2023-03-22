package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactions.getNumberOfDays
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.spanButtonB2
import lib.dehaat.ledger.resources.spanParagraphT2Highlight
import lib.dehaat.ledger.resources.textParagraphT2Highlight

@Preview(
    showBackground = true
)
@Composable
private fun FilterHeaderPreview() = LedgerTheme {
    FilterHeader(DaysToFilter.All) {}
}

@Composable
fun FilterHeader(
    filter: DaysToFilter,
    onFilterClick: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onFilterClick)
        .padding(
            horizontal = 20.dp,
            vertical = 6.dp
        ),
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_filter),
        contentDescription = stringResource(id = R.string.accessibility_icon)
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
        modifier = Modifier.padding(vertical = 2.dp),
        text = getTitle(filter = filter),
        style = textParagraphT2Highlight(Neutral80)
    )

    Spacer(modifier = Modifier.width(4.dp))

    Icon(
        painter = painterResource(id = R.drawable.ic_expand_more),
        contentDescription = stringResource(id = R.string.accessibility_icon)
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun getTitle(filter: DaysToFilter) = buildAnnotatedString {
    withStyle(spanParagraphT2Highlight(Neutral80)) {
        append(stringResource(id = R.string.filter_colon))
    }
    append(" ")
    withStyle(spanButtonB2(Neutral80)) {
        append(
            filter.getNumberOfDays()?.let {
                stringResource(
                    id = R.string.last_n_days,
                    it
                )
            } ?: stringResource(id = R.string.transactions_so_far)
        )
    }
}
