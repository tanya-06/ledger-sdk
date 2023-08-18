package lib.dehaat.ledger.presentation.mapper

import com.dehaat.androidbase.helper.orZero
import com.dehaat.androidbase.utils.DateUtils
import lib.dehaat.ledger.entities.creditsummary.CreditSummaryEntity
import lib.dehaat.ledger.entities.revamp.creditsummary.CreditSummaryEntityV2
import lib.dehaat.ledger.entities.transactionsummary.ABSEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit.AvailableCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.outstandingcreditlimit.OutstandingCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation.OutstandingCalculationUiState
import lib.dehaat.ledger.presentation.ledger.state.LedgerTotalCalculation
import lib.dehaat.ledger.presentation.ledger.ui.component.orZero
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.presentation.model.revamp.WidgetsViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldABSViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldAmountViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.TransactionSummaryViewData
import lib.dehaat.ledger.util.appendSignIfRequired
import lib.dehaat.ledger.util.formatDecimal
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.util.getRoundedAmountInRupees
import lib.dehaat.ledger.util.isNullOrZero
import lib.dehaat.ledger.util.toDateMonthName
import lib.dehaat.ledger.util.toDoubleOrZero
import java.util.Locale
import javax.inject.Inject

class ViewDataMapper @Inject constructor() {

	fun toCreditSummaryViewData(entity: CreditSummaryEntityV2) = with(entity) {
		val isOrderingBlocked =
			minimumRepaymentAmount.toDoubleOrZero() > 0 && overdueAmount.toDoubleOrZero() > overdueCreditLimit.toDoubleOrZero()
		val isCreditLineOnHold = when {
			isOrderingBlocked -> true
			creditLineStatus == LedgerConstants.ON_HOLD -> false
			else -> minimumRepaymentAmount.toDoubleOrZero() <= 0 || repaymentDate == null
		}
		SummaryViewData(
			bufferLimit = bufferLimit,
			creditNoteAmountTillDate = creditNoteAmountTillDate,
			externalFinancierSupported = externalFinancierSupported,
			interestTillDate = interestTillDate,
			minInterestAmountDue = minInterestAmountDue,
			minInterestOutstandingDate = minInterestOutstandingDate,
			minOutstandingAmountDue = minOutstandingAmountDue,
			paymentAmountTillDate = paymentAmountTillDate,
			permanentCreditLimit = permanentCreditLimit,
			purchaseAmountTillDate = purchaseAmountTillDate,
			totalAvailableCreditLimit = totalAvailableCreditLimit,
			totalCreditLimit = totalCreditLimit,
			totalOutstandingAmount = totalOutstandingAmount,
			totalPurchaseAmount = totalPurchaseAmount,
			undeliveredInvoiceAmount = undeliveredInvoiceAmount,
			totalInterestOutstanding = totalInterestOutstanding,
			totalInterestPaid = totalInterestPaid,
			minimumRepaymentAmount = minimumRepaymentAmount,
			hideMinimumRepaymentSection = minimumRepaymentAmount.toDoubleOrZero() <= 0 || repaymentDate == null,
			isOrderingBlocked = isOrderingBlocked,
			repaymentDate = repaymentDate.toDateMonthName(),
			showToolTipInformation = overdueAmount.toDoubleOrZero() > 0 && isCreditLineOnHold && minimumRepaymentAmount.toDoubleOrZero() > 0,
			creditLineStatus = creditLineStatus,
			creditLineSubStatus = creditLineSubStatus,
			agedOutstandingAmount = formatDecimal(agedOutstandingAmount, 0),
			repaymentUnblockAmount = formatDecimal(repaymentUnblockAmount, 0),
			isCreditLineOnHold = isCreditLineOnHold,
			holdAmount = holdAmount,
			ageingBannerPriority = ageingBannerPriority,
			penaltyInterest = penaltyInterest,
			agedOverdueAmount = agedOverdueAmount?.toString().getAmountInRupees(),
			firstLedgerEntryDate = firstLedgerEntryDate,
			ledgerEndDate = ledgerEndDate,
			showOverdueWidget = ledgerOverdueAmount != null && overdueStatus == null,
			showOrderingBlockedWidget = ledgerOverdueAmount != null && overdueStatus != null,
			ledgerOverdueAmount = ledgerOverdueAmount.orZero(),
			ledgerEarliestOverdueDate = DateUtils.getDateInFormat(
				LedgerConstants.dd_MMM,
				ledgerEarliestOverdueDate?.toFloat().orZero(),
				Locale.getDefault()
			),
			showInterestWidget = ledgerInterestAmount != null && interestStatus != null,
			ledgerInterestAmount = ledgerInterestAmount.orZero(),
			ledgerEarliestInterestDate = DateUtils.getDateInFormat(
				LedgerConstants.dd_MMM, ledgerEarliestInterestDate?.toFloat().orZero(),
				Locale.getDefault()
			),
			showInterestNotStartedWidget = ledgerInterestAmount != null && interestStatus == null
		)
	}

	fun toAvailableCreditLimitViewState(entity: CreditSummaryEntityV2) = with(entity) {
		AvailableCreditLimitViewState(
			totalAvailableCreditLimit = totalAvailableCreditLimit,
			totalCreditLimit = totalCreditLimit,
			outstandingAndDeliveredAmount = (totalOutstandingAmount.toDouble() + undeliveredInvoiceAmount.toDouble()).toString(),
			permanentCreditLimit = permanentCreditLimit,
			bufferLimit = bufferLimit,
			totalLimit = (permanentCreditLimit.toDouble() + bufferLimit.toDouble()).toString()
		)
	}

	fun toOutstandingCreditLimitViewState(entity: CreditSummaryEntityV2) = with(entity) {
		OutstandingCreditLimitViewState(
			totalOutstandingAmount,
			totalPurchaseAmount,
			interestTillDate,
			paymentAmountTillDate,
			purchaseAmountTillDate,
			creditNoteAmountTillDate
		)
	}

	fun toTransactionSummaryViewData(
		entity: TransactionSummaryEntity
	) = TransactionSummaryViewData(
		purchaseAmount = entity.purchaseAmount.getRoundedAmountInRupees(),
		paymentAmount = entity.paymentAmount.getRoundedAmountInRupees(),
		interestAmount = entity.interestAmount.getRoundedAmountInRupees(),
		holdAmountViewData = entity.toHoldAmountViewData(),
		debitHoldAmount = entity.debitHoldAmount.getAmountInRupees(),
		releaseAmount = entity.releasePaymentAmount.getAmountInRupees(),
	)

	private fun toABSViewData(abs: ABSEntity?) = abs?.run {
		ABSViewData(
			amount,
			lastMoveScheme,
			showBanner,
			lastMovedSchemeAmount?.let { it.getAmountInRupees() })
	}

	fun toOutstandingCalculationViewData(entity: TransactionSummaryEntity) = with(entity) {
		OutstandingCalculationUiState(
			totalOutstanding =
			(purchaseAmount.toDoubleOrZero() - netPaymentAmount.toDoubleOrZero()).toString()
				.getAmountInRupees().appendSignIfRequired("+"),
			totalPurchase = purchaseAmount.getAmountInRupees().appendSignIfRequired("+"),
			totalPayment = netPaymentAmount.getAmountInRupees().appendSignIfRequired("-"),
			totalInvoiceAmount = totalInvoiceAmount.getAmountInRupees()
				.appendSignIfRequired("+"),
			totalCreditNoteAmount = creditNoteAmount.getAmountInRupees()
				.appendSignIfRequired("-"),
			showCreditNodeAmount = !creditNoteAmount.isNullOrZero(),
			outstandingInterestAmount = interestOutstanding.getAmountInRupees()
				.appendSignIfRequired("+"),
			showOutstandingInterestAmount = !interestOutstanding.isNullOrZero(),
			paidInterestAmount = interestPaid.getAmountInRupees().appendSignIfRequired("+"),
			showPaidInterestAmount = !interestPaid.isNullOrZero(),
			creditNoteAmount = if (totalInterestRefundAmount.isNullOrZero()) {
				null
			} else {
				totalInterestRefundAmount.getAmountInRupees().appendSignIfRequired("-")
			},
			totalDebitNoteAmount = debitNodeAmount.getAmountInRupees()
				.appendSignIfRequired("+"),
			showDebitNodeAmount = !debitNodeAmount.isNullOrZero(),
			paidAmount = paymentAmount.getAmountInRupees().appendSignIfRequired("-"),
			paidRefund = debitEntryAmount.getAmountInRupees().appendSignIfRequired("+"),
			totalPaid = netPaymentAmount.getAmountInRupees().appendSignIfRequired("-"),
			debitHold = debitHoldAmount.getAmountInRupees().appendSignIfRequired("+"),
			paymentReleased = releasePaymentAmount.getAmountInRupees().appendSignIfRequired("-")
		)
	}

	fun toLedgerTotalCalculation(response: TransactionSummaryEntity?) = response?.let {
		LedgerTotalCalculation(
			totalPurchase = it.purchaseAmount.getRoundedAmountInRupees(),
			totalPayment = it.netPaymentAmount.getRoundedAmountInRupees(),
		)
	}

	fun toAbsViewData(abs: ABSEntity?) = abs?.let { entity ->
		ABSViewData(
			entity.amount,
			entity.lastMoveScheme,
			entity.showBanner,
			entity.lastMovedSchemeAmount?.let { it.getAmountInRupees() }
		)
	}

	fun toHoldAmountViewData(data: TransactionSummaryEntity?) = data?.toHoldAmountViewData()
	fun toWidgetsViewData(creditSummary: CreditSummaryEntityV2) = with(creditSummary) {
		WidgetsViewData(
			creditLineStatus = creditLineStatus,
			creditLineSubStatus = creditLineSubStatus,
			agedOutstandingAmount = formatDecimal(agedOutstandingAmount, 0),
			repaymentUnblockAmount = formatDecimal(repaymentUnblockAmount, 0),
			ageingBannerPriority = ageingBannerPriority,
			showOverdueWidget = ledgerOverdueAmount != null && overdueStatus == null,
			showOrderingBlockedWidget = ledgerOverdueAmount != null && overdueStatus != null,
			ledgerOverdueAmount = ledgerOverdueAmount.orZero(),
			ledgerEarliestOverdueDate = DateUtils.getDateInFormat(
				LedgerConstants.dd_MMM,
				ledgerEarliestOverdueDate?.toFloat().orZero(),
				Locale.getDefault()
			),
			showInterestWidget = ledgerInterestAmount != null && interestStatus != null,
			ledgerInterestAmount = ledgerInterestAmount.orZero(),
			ledgerEarliestInterestDate = DateUtils.getDateInFormat(
				LedgerConstants.dd_MMM, ledgerEarliestInterestDate?.toFloat().orZero(),
				Locale.getDefault()
			),
			showInterestNotStartedWidget = ledgerInterestAmount != null && interestStatus == null
		)
	}

	fun toWidgetsViewData(creditSummary: CreditSummaryEntity) = with(creditSummary) {
		WidgetsViewData(
			creditLineStatus = null,
			creditLineSubStatus = "",
			agedOutstandingAmount = "",
			repaymentUnblockAmount = "",
			ageingBannerPriority = null,
			showOverdueWidget = info.ledgerOverdueAmount != null && info.overdueStatus == null,
			showOrderingBlockedWidget = info.ledgerOverdueAmount != null && info.overdueStatus != null,
			showInterestWidget = false,
			ledgerOverdueAmount = info.ledgerOverdueAmount.orZero(),
			ledgerInterestAmount = 0.0,
			ledgerEarliestInterestDate = "",
			showInterestNotStartedWidget = false,
			ledgerEarliestOverdueDate = DateUtils.getDateInFormat(
				LedgerConstants.dd_MMM,
				info.ledgerEarliestOverdueDate?.toFloat().orZero(),
				Locale.getDefault()
			)
		)
	}

}

fun TransactionSummaryEntity.toHoldAmountViewData() = HoldAmountViewData(
	formattedTotalHoldBalance = getTotalHoldBalance(this).getAmountInRupees(),
	formattedPrepaidHoldAmount = this.prepaidHoldAmount.getAmountInRupees(),
	absViewData = toHoldABSViewData(abs),
	prepaidHoldAmount = this.prepaidHoldAmount
)

private fun getTotalHoldBalance(entity: TransactionSummaryEntity) =
	(entity.prepaidHoldAmount ?: 0.0).plus(entity.abs?.amount ?: 0.0)

private fun toHoldABSViewData(abs: ABSEntity?) = HoldABSViewData(
	formattedAbsHoldBalance = abs?.amount.getAmountInRupees(),
	formattedLastMovedSchemeAmount = abs?.lastMovedSchemeAmount.getAmountInRupees(),
	showBanner = true == abs?.showBanner,
	absHoldBalance = abs?.amount
)