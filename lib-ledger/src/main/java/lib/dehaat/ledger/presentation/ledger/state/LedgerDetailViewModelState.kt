package lib.dehaat.ledger.presentation.ledger.state

import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.outstanding.LedgerOutStandingDetailViewData
import lib.dehaat.ledger.presentation.model.outstanding.OverAllOutStandingDetailViewData
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter
import lib.dehaat.ledger.presentation.model.transactionsummary.TransactionSummaryViewData

data class LedgerDetailViewModelState(
    val creditSummaryViewData: CreditSummaryViewData? = null,
    val overAllOutStandingDetailViewData: OverAllOutStandingDetailViewData = OverAllOutStandingDetailViewData(
        principleOutstanding = "",
        interestOutstanding = "",
        overdueInterestOutstanding = "",
        penaltyOutstanding = "",
        undeliveredInvoices = "",
        creditLinesUsed = listOf()
    ),
    val lenderOutStandingDetailViewData: LedgerOutStandingDetailViewData = LedgerOutStandingDetailViewData(
        outstanding = "",
        principleOutstanding = "",
        interestOutstanding = "",
        overdueInterestOutstanding = "",
        penaltyOutstanding = "",
        advanceAmount = "",
        sanctionedCreditLimit = "",
        lenderName = ""
    ),
    val selectedDaysFilter: DaysToFilter? = null,
    val bottomSheetType: BottomSheetType = BottomSheetType.OverAllOutStanding(
        data = overAllOutStandingDetailViewData
    ),
    val isLoading: Boolean = false,
    val transactionSummaryViewData: TransactionSummaryViewData? = null,
    val showFilterRangeDialog: Boolean = false
) {
    fun toUiState() = LedgerDetailUIState(
        creditSummaryViewData = creditSummaryViewData,
        isLoading = isLoading,
        bottomSheetType = bottomSheetType,
        transactionSummaryViewData = transactionSummaryViewData,
        isFilteringWithRange = showFilterRangeDialog
    )
}

data class LedgerDetailUIState(
    val creditSummaryViewData: CreditSummaryViewData?,
    val isLoading: Boolean,
    val bottomSheetType: BottomSheetType,
    val transactionSummaryViewData: TransactionSummaryViewData?,
    val isFilteringWithRange: Boolean
)

sealed class BottomSheetType {
    data class OverAllOutStanding(val data: OverAllOutStandingDetailViewData) :
        BottomSheetType()

    data class LenderOutStanding(val data: LedgerOutStandingDetailViewData) :
        BottomSheetType()

    data class DaysFilterTypeSheet(val selectedFilter: DaysToFilter?) : BottomSheetType()
}