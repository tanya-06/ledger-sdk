package lib.dehaat.ledger.presentation.ledger.state

import androidx.annotation.StringRes
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation.OutstandingCalculationUiState
import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldAmountViewData
import lib.dehaat.ledger.presentation.model.transactions.DaysToFilter

data class LedgerHomeScreenViewModelState(
	val outstandingAmount: String = "",
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val errorMessage: String? = null,
	val ledgerTotalCalculation: LedgerTotalCalculation? = null,
	val outstandingCalculationUiState: OutstandingCalculationUiState? = null,
	val holdAmountViewData: HoldAmountViewData? = null,
	val isLMSActivated: Boolean = false
) {
	fun toUiState() = HomeScreenUiState(
		state = when {
			isLoading -> UIState.LOADING
			isError -> UIState.ERROR(errorMessage.orEmpty())
			else -> UIState.SUCCESS
		},
		outstandingAmount = outstandingAmount,
		ledgerTotalCalculation = ledgerTotalCalculation,
		outstandingCalculationUiState = outstandingCalculationUiState,
		holdAmountViewData = holdAmountViewData
	)
}

data class HomeScreenUiState(
	val state: UIState,
	val outstandingAmount: String,
	val ledgerTotalCalculation: LedgerTotalCalculation?,
	val outstandingCalculationUiState: OutstandingCalculationUiState?,
	val holdAmountViewData: HoldAmountViewData?
)

data class LedgerTotalCalculation(
	val totalPurchase: String,
	val totalPayment: String
)

sealed class LedgerTransactions(
	@StringRes val name: Int,
	val date: Long
) {
	data class Invoice(
		val invoiceName: Int = R.string.invoice,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = invoiceName,
		date = invoiceDate
	)

	data class CreditNote(
		val creditNoteName: Int = R.string.credit_note_ledger,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = creditNoteName,
		date = invoiceDate
	)

	data class Payment(
		val paymentName: Int = R.string.ledger_payment,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = paymentName,
		date = invoiceDate
	)

	data class ReleasePayment(
		val paymentName: Int = R.string.hold_payment_released_prepaid,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = paymentName, date = invoiceDate
	)

	data class DebitHold(
		val paymentName: Int = R.string.dc_debit_hold_entry_title,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = paymentName, date = invoiceDate
	)

	data class Interest(
		val interestName: Int = R.string.interest_amount_ledger,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = interestName,
		date = invoiceDate
	)

	data class FinancingFee(
		val financingFeeName: Int = R.string.financing_fee,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = financingFeeName,
		date = invoiceDate
	)

	data class DebitNote(
		val debitNoteName: Int = R.string.debit_note,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = debitNoteName,
		date = invoiceDate
	)

	data class DebitEntry(
		val debitEntryName: Int = R.string.debit_entry,
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = debitEntryName,
		date = invoiceDate
	)

	data class MonthSeparator(
		val monthType: String = "MONTH",
		val invoiceDate: Long,
		val transactionsViewData: TransactionViewData
	) : LedgerTransactions(
		name = 0,
		date = invoiceDate
	) {
		companion object {
			fun monthlySeparator(month: String) = MonthSeparator(
				invoiceDate = 0,
				transactionsViewData = TransactionViewData(
					amount = "",
					creditNoteReason = null,
					date = 0L,
					erpId = null,
					interestEndDate = null,
					interestStartDate = null,
					ledgerId = "",
					locusId = null,
					partnerId = "",
					paymentMode = null,
					source = "",
					sourceNo = null,
					type = TransactionType.MonthSeparator().type,
					unrealizedPayment = null,
					fromDate = month,
					toDate = null,
					adjustmentAmount = null,
					schemeName = null,
					creditAmount = null,
					prepaidAmount = null
				)
			)
		}
	}
}

data class TransactionViewData(
	val amount: String,
	val creditNoteReason: String?,
	val date: Long,
	val erpId: String?,
	val interestEndDate: Long?,
	val interestStartDate: Long?,
	val ledgerId: String,
	val locusId: Int?,
	val partnerId: String,
	val paymentMode: String?,
	val source: String,
	val sourceNo: String?,
	val type: String,
	val unrealizedPayment: Boolean?,
	val fromDate: String?,
	val toDate: String?,
	val adjustmentAmount: Double?,
	val schemeName: String?,
	val creditAmount: String?,
	val prepaidAmount: String?,
)

data class LedgerFilterViewModelState(
	val showFilterSheet: Boolean = false,
	val showWeeklyInterestDecreasingLabel: Boolean = false,
	val appliedFilter: DaysToFilter = DaysToFilter.All
) {
	fun toUiState() = LedgerFilterUIState(
		showFilterSheet = showFilterSheet,
		showWeeklyInterestDecreasingLabel = showWeeklyInterestDecreasingLabel,
		appliedFilter = appliedFilter
	)
}

data class LedgerFilterUIState(
	val showFilterSheet: Boolean,
	val showWeeklyInterestDecreasingLabel: Boolean,
	val appliedFilter: DaysToFilter
)
