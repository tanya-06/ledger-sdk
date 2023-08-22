package lib.dehaat.ledger.presentation.ledger.details.payments.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lib.dehaat.ledger.R
import lib.dehaat.ledger.data.dummy.DummyDataSource
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.detail.payment.PaymentDetailSummaryViewData
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.BlueGreen10
import lib.dehaat.ledger.resources.FrenchBlue120
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral100
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.textHeadingH3
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.util.toDateMonthYear

@Preview(
    showBackground = true,
    name = "RevampPaymentDetailScreen Preview"
)
@Composable
private fun PaymentDetailsScreenPreview() = LedgerTheme {
    PaymentDetails(DummyDataSource.paymentDetailSummaryViewData)
}

@Composable
fun RevampPaymentDetailScreen(
    viewModel: PaymentDetailViewModel = hiltViewModel(),
    ledgerColors: LedgerColors,
    onError: (Exception) -> Unit,
    onBackPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val paymentSummary = uiState.paymentDetailSummaryViewData
    CommonContainer(
        title = stringResource(id = R.string.payment_detail),
        onBackPress = onBackPress,
        backgroundColor = Background,
        ledgerColors = ledgerColors
    ) {
        when (uiState.state) {
            UIState.SUCCESS -> {
                PaymentDetails(paymentSummary, viewModel.unrealizedPayment)
            }
            UIState.LOADING -> {
                ShowProgressDialog(ledgerColors) {
                    viewModel.updateProgressDialog(false)
                }
            }
            is UIState.ERROR -> {
                NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
            }
        }
    }
}

@Composable
private fun PaymentDetails(
    paymentSummary: PaymentDetailSummaryViewData?,
    unrealizedPayment: Boolean? = false
) = Column(
    modifier = Modifier.fillMaxWidth()
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            paymentSummary?.schemeName?.let {
                Text(
                    modifier = Modifier
                        .background(color = BlueGreen10, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    text = it,
                    style = textParagraphT2Highlight(FrenchBlue120)
                )
                VerticalSpacer(height = 20.dp)
            }

            Text(
                text = stringResource(id = R.string.payment_amount),
                style = textParagraphT1Highlight(Neutral90)
            )

            VerticalSpacer(height = 4.dp)

            Text(
                text = paymentSummary?.totalAmount.getAmountInRupees(),
                style = textHeadingH3(Neutral80)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = stringResource(id = R.string.accessibility_icon)
        )
    }

    VerticalSpacer(height = 16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.payment_date),
                style = textParagraphT2Highlight(Neutral80)
            )

            Text(
                text = paymentSummary?.timestamp.toDateMonthYear(),
                style = textParagraphT2Highlight(Neutral80)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.payment_method),
                style = textParagraphT2Highlight(Neutral80)
            )

            Text(
                text = paymentSummary?.mode ?: "",
                style = textParagraphT2Highlight(Neutral80)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.reference_id),
                style = textParagraphT2Highlight(Neutral80)
            )

            Text(
                text = paymentSummary?.referenceId ?: "",
                style = textParagraphT2Highlight(Neutral80)
            )
        }
    }
    if (unrealizedPayment == true) {
        VerticalSpacer(height = 16.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.information_),
                    style = textParagraphT2Highlight(Neutral100)
                )
                Text(
                    text = stringResource(R.string.payment_will_be_processed_within_1_2_days),
                    style = textParagraphT2Highlight(Neutral100),
                    modifier = Modifier.padding(start = 10.dp)

                )
            }
        }
    }
}
