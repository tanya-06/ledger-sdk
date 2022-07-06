package lib.dehaat.ledger.presentation.mapper

import lib.dehaat.ledger.entities.creditlines.CreditLineEntity
import lib.dehaat.ledger.entities.creditsummary.CreditEntity
import lib.dehaat.ledger.entities.creditsummary.CreditSummaryEntity
import lib.dehaat.ledger.entities.creditsummary.InfoEntity
import lib.dehaat.ledger.entities.creditsummary.OverdueEntity
import lib.dehaat.ledger.entities.detail.creditnote.CreditNoteDetailEntity
import lib.dehaat.ledger.entities.detail.creditnote.ProductEntity
import lib.dehaat.ledger.entities.detail.creditnote.ProductsInfoEntity
import lib.dehaat.ledger.entities.detail.creditnote.SummaryEntity
import lib.dehaat.ledger.entities.detail.invoice.InvoiceDetailDataEntity
import lib.dehaat.ledger.entities.detail.invoice.LoanEntity
import lib.dehaat.ledger.entities.transactions.TransactionEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.presentation.model.creditlines.CreditLineViewData
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.creditsummary.CreditViewData
import lib.dehaat.ledger.presentation.model.creditsummary.InfoViewData
import lib.dehaat.ledger.presentation.model.creditsummary.OverdueViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.CreditNoteDetailViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.ProductViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.ProductsInfoViewData
import lib.dehaat.ledger.presentation.model.detail.creditnote.SummaryViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.InvoiceDetailDataViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.LoanViewData
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.presentation.model.transactionsummary.TransactionSummaryViewData
import javax.inject.Inject

typealias ViewDataPaymentDetailSummary = lib.dehaat.ledger.presentation.model.detail.payment.PaymentDetailSummaryViewData
typealias EntityPaymentDetailSummary = lib.dehaat.ledger.entities.detail.payment.SummaryEntity
typealias ViewDataInvoiceDetailSummary = lib.dehaat.ledger.presentation.model.detail.invoice.SummaryViewData
typealias EntityInvoiceDetailSummary = lib.dehaat.ledger.entities.detail.invoice.SummaryEntity
typealias ViewDataInvoiceDetailProductsInfo = lib.dehaat.ledger.presentation.model.detail.invoice.ProductsInfoViewData
typealias EntityInvoiceDetailProductsInfo = lib.dehaat.ledger.entities.detail.invoice.ProductsInfoEntity
typealias ViewDataInvoiceDetailProduct = lib.dehaat.ledger.presentation.model.detail.invoice.ProductViewData
typealias EntityInvoiceDetailProduct = lib.dehaat.ledger.entities.detail.invoice.ProductEntity

class LedgerViewDataMapper @Inject constructor() {

    fun toCreditSummaryViewData(data: CreditSummaryEntity) = with(data) {
        CreditSummaryViewData(
            credit = toCreditSummaryCreditViewData(credit),
            overdue = toCreditSummaryOverDueViewData(overdue),
            info = toCreditSummaryInfoViewData(info),
        )
    }

    fun toTransactionSummaryViewData(
        data: TransactionSummaryEntity
    ) = with(data) {
        TransactionSummaryViewData(
            purchaseAmount = purchaseAmount,
            paymentAmount = paymentAmount
        )
    }

    fun toCreditLinesViewData(data: List<CreditLineEntity>) = data.map {
        toCreditLineViewData(it)
    }

    fun toTransactionsDataEntity(data: List<TransactionEntity>) = data.map {
        toTransactionViewData(it)
    }

    fun toCreditNoteDetailDataEntity(data: CreditNoteDetailEntity) = with(data) {
        CreditNoteDetailViewData(
            summary = getCreditNoteDetailSummaryViewData(summary),
            productsInfo = getCreditNoteDetailProductInfoViewData(productsInfo),
        )
    }

    fun toPaymentDetailSummaryViewData(data: EntityPaymentDetailSummary) = with(data) {
        ViewDataPaymentDetailSummary(
            referenceId = referenceId,
            timestamp = timestamp,
            totalAmount = totalAmount,
            mode = mode,
            principalComponent = principalComponent,
            interestComponent = interestComponent,
            overdueInterestComponent = overdueInterestComponent,
            penaltyComponent = penaltyComponent,
            advanceComponent = advanceComponent,
            paidTo = paidTo,
            belongsToGapl = belongsToGapl
        )
    }

    fun toInvoiceDetailDataViewData(data: InvoiceDetailDataEntity) = with(data) {
        InvoiceDetailDataViewData(
            summary = getInvoiceDetailSummaryViewData(summary),
            loans = loans.map { getInvoiceDetailLoanViewData(it) },
            productsInfo = getInvoiceDetailProductInfoViewData(productsInfo),
        )
    }

    private fun getInvoiceDetailProductInfoViewData(data: EntityInvoiceDetailProductsInfo) =
        with(data) {
            ViewDataInvoiceDetailProductsInfo(
                count = count,
                discount = discount,
                gst = gst,
                itemTotal = itemTotal,
                subTotal = subTotal,
                productList = productList.map {
                    getInvoiceDetailProductViewData(it)
                }
            )
        }

    private fun getInvoiceDetailProductViewData(it: EntityInvoiceDetailProduct) =
        with(it) {
            ViewDataInvoiceDetailProduct(
                fname = fname,
                name = name,
                priceTotal = priceTotal,
                priceTotalDiscexcl = priceTotalDiscexcl,
                quantity = quantity
            )
        }

    private fun getInvoiceDetailLoanViewData(data: LoanEntity) = with(data) {
        LoanViewData(
            loanAccountNo = loanAccountNo,
            status = status,
            amount = amount,
            invoiceContributionInLoan = invoiceContributionInLoan,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            disbursalDate = disbursalDate,
            interestFreeEndDate = interestFreeEndDate,
            financier = financier,
            belongsToGapl = belongsToGapl
        )
    }

    private fun getInvoiceDetailSummaryViewData(data: EntityInvoiceDetailSummary) = with(data) {
        ViewDataInvoiceDetailSummary(
            amount = amount,
            number = number,
            timestamp = timestamp
        )
    }

    private fun getCreditNoteDetailProductInfoViewData(data: ProductsInfoEntity) = with(data) {
        ProductsInfoViewData(
            count = count,
            gst = gst,
            itemTotal = itemTotal,
            subTotal = subTotal,
            productList = productList.map {
                getCreditNoteDetailProductViewData(it)
            }
        )
    }

    private fun getCreditNoteDetailProductViewData(it: ProductEntity) =
        with(it) {
            ProductViewData(
                fname = fname,
                name = name,
                priceTotal = priceTotal,
                priceTotalDiscexcl = priceTotalDiscexcl,
                quantity = quantity
            )
        }

    private fun getCreditNoteDetailSummaryViewData(data: SummaryEntity) = with(data) {
        SummaryViewData(
            amount = amount,
            invoiceNumber = invoiceNumber,
            timestamp = timestamp,
            reason = reason
        )
    }

    private fun toTransactionViewData(data: TransactionEntity) = with(data) {
        TransactionViewData(
            ledgerId = ledgerId,
            type = type,
            date = date,
            amount = amount,
            erpId = erpId,
            locusId = locusId,
            creditNoteReason = creditNoteReason,
            paymentMode = paymentMode
        )
    }

    private fun toCreditLineViewData(data: CreditLineEntity) = with(data) {
        CreditLineViewData(
            belongsToGapl = belongsToGapl,
            lenderViewName = lenderViewName,
            creditLimit = creditLimit,
            availableCreditLimit = availableCreditLimit,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount,
            advanceAmount = advanceAmount
        )
    }

    private fun toCreditSummaryInfoViewData(data: InfoEntity) = with(data) {
        InfoViewData(
            totalPurchaseAmount = totalPurchaseAmount,
            totalPaymentAmount = totalPaymentAmount,
            undeliveredInvoiceAmount = undeliveredInvoiceAmount
        )
    }

    private fun toCreditSummaryOverDueViewData(data: OverdueEntity) = with(data) {
        OverdueViewData(
            totalOverdueLimit = totalOverdueLimit,
            totalOverdueAmount = totalOverdueAmount,
            minPaymentAmount = minPaymentAmount,
            minPaymentDueDate = minPaymentDueDate
        )
    }

    private fun toCreditSummaryCreditViewData(data: CreditEntity) = with(data) {
        CreditViewData(
            externalFinancierSupported = externalFinancierSupported,
            totalCreditLimit = totalCreditLimit,
            totalAvailableCreditLimit = totalAvailableCreditLimit,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount
        )
    }
}