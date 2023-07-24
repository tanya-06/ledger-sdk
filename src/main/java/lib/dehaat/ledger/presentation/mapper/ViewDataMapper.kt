package lib.dehaat.ledger.presentation.mapper

import javax.inject.Inject
import lib.dehaat.ledger.entities.revamp.creditsummary.CreditSummaryEntityV2
import lib.dehaat.ledger.entities.transactionsummary.ABSEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.initializer.formatDecimal
import lib.dehaat.ledger.initializer.toDateMonthName
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit.AvailableCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.outstandingcreditlimit.OutstandingCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation.OutstandingCalculationUiState
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldABSViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.HoldAmountViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.TransactionSummaryViewData
import lib.dehaat.ledger.util.getRoundedAmountInRupees
import lib.dehaat.ledger.util.toDoubleOrZero
import lib.dehaat.ledger.util.getAmountInRupees

class ViewDataMapper @Inject constructor() {

    fun toCreditSummaryViewData(entity: CreditSummaryEntityV2) = with(entity) {
        val isOrderingBlocked = minimumRepaymentAmount.toDoubleOrZero() > 0 && overdueAmount.toDoubleOrZero() > overdueCreditLimit.toDoubleOrZero()
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
            creditLineStatus = if (isOrderingBlocked) null else creditLineStatus,
            creditLineSubStatus = creditLineSubStatus,
            agedOutstandingAmount = formatDecimal(agedOutstandingAmount, 0),
            repaymentUnblockAmount = formatDecimal(repaymentUnblockAmount, 0),
            isCreditLineOnHold = isCreditLineOnHold,
            holdAmount = holdAmount
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
            totalOutstanding = "+ ${(purchaseAmount.toDoubleOrZero() - netPaymentAmount.toDoubleOrZero()).toString()
                .getRoundedAmountInRupees()}",
            totalPurchase = "+ ${purchaseAmount.getRoundedAmountInRupees()}",
            totalPayment = netPaymentAmount.getRoundedAmountInRupees(),
            totalInvoiceAmount = "+ ${totalInvoiceAmount.getRoundedAmountInRupees()}",
            totalCreditNoteAmount = "- ${creditNoteAmount.getRoundedAmountInRupees()}",
            outstandingInterestAmount = "+ ${interestOutstanding.getRoundedAmountInRupees()}",
            paidInterestAmount = "+ ${interestPaid.getRoundedAmountInRupees()}",
            creditNoteAmount = "- ${totalInterestRefundAmount.getRoundedAmountInRupees()}",
            totalDebitNoteAmount = "+ ${debitNodeAmount.getRoundedAmountInRupees()}",
            paidAmount = "+ ${paymentAmount.getRoundedAmountInRupees()}",
            paidRefund = "+ ${debitEntryAmount.getRoundedAmountInRupees()}",
            totalPaid = "- ${netPaymentAmount.getRoundedAmountInRupees()}",
            debitHold = "+ ${debitHoldAmount.getAmountInRupees()}",
            paymentReleased = "- ${releasePaymentAmount.getAmountInRupees()}", )
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