package lib.dehaat.ledger.presentation.model.transactionsummary

import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldAmountViewData

data class TransactionSummaryViewData(
    val purchaseAmount: String,
    val paymentAmount: String,
    val holdAmountViewData: HoldAmountViewData,
    val debitHoldAmount: String,
    val releaseAmount: String,
)
