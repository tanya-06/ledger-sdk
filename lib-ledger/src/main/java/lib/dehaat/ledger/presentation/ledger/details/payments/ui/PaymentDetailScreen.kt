package lib.dehaat.ledger.presentation.ledger.details.payments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.initializer.getAmountInRupees
import lib.dehaat.ledger.initializer.themes.LedgerColors
import lib.dehaat.ledger.initializer.toDateMonthYear
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValue
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryView
import lib.dehaat.ledger.presentation.ledger.components.CreditNoteKeyValueInSummaryViewWithTopPadding
import lib.dehaat.ledger.presentation.ledger.details.payments.PaymentDetailViewModel
import lib.dehaat.ledger.resources.text18Sp

@Composable
fun PaymentDetailScreen(
    viewModel: PaymentDetailViewModel,
    ledgerColors: LedgerColors,
    onBackPress: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val paymentSummary = uiState.paymentDetailSummaryViewData
    val scrollState = rememberScrollState()

    CommonContainer(
        title = "Payment Detail",
        onBackPress = onBackPress,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState,
                    enabled = true,
                )
                .background(Color.White)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CreditNoteKeyValue(
                "Payment Amount",
                paymentSummary?.totalAmount.getAmountInRupees(),
                keyTextStyle = text18Sp(textColor = ledgerColors.CtaDarkColor),
                valueTextStyle = text18Sp(
                    fontWeight = FontWeight.Bold,
                    textColor = ledgerColors.CtaDarkColor
                ),
            )

            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(
                        shape = RoundedCornerShape(9.dp),
                        color = ledgerColors.InfoContainerBgColor
                    )
                    .padding(16.dp)
            ) {

                CreditNoteKeyValueInSummaryView(
                    "Principal Paid", paymentSummary?.principalComponent.getAmountInRupees(),
                    ledgerColors = ledgerColors,
                )

                CreditNoteKeyValueInSummaryViewWithTopPadding(
                    "Penalty Paid",
                    paymentSummary?.penaltyComponent.getAmountInRupees(),
                    ledgerColors = ledgerColors,
                )

                CreditNoteKeyValueInSummaryViewWithTopPadding(
                    "Advance Paid",
                    paymentSummary?.advanceComponent.getAmountInRupees(),
                    ledgerColors = ledgerColors,
                )

                CreditNoteKeyValueInSummaryViewWithTopPadding(
                    "Payment Method",
                    viewModel.paymentMode ?: "",
                    ledgerColors = ledgerColors,
                )
                CreditNoteKeyValueInSummaryViewWithTopPadding(
                    "Reference ID",
                    paymentSummary?.referenceId ?: "",

                    ledgerColors = ledgerColors,
                )
                CreditNoteKeyValueInSummaryViewWithTopPadding(
                    "Payment Date",
                    paymentSummary?.timestamp.toDateMonthYear(),

                    ledgerColors = ledgerColors,
                )
                CreditNoteKeyValueInSummaryViewWithTopPadding(
                    "Paid To",
                    paymentSummary?.paidTo ?: "",
                    ledgerColors = ledgerColors,
                )

            }
        }
    }
}