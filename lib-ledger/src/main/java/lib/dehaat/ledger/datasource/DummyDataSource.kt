package lib.dehaat.ledger.datasource

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import lib.dehaat.ledger.initializer.LedgerParentApp
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.callbacks.LedgerCallbacks
import lib.dehaat.ledger.presentation.model.creditlines.CreditLineViewData
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.creditsummary.CreditViewData
import lib.dehaat.ledger.presentation.model.creditsummary.InfoViewData
import lib.dehaat.ledger.presentation.model.creditsummary.OverdueViewData
import lib.dehaat.ledger.presentation.model.detail.invoice.InvoiceDetailDataViewData
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData

object DummyDataSource {

    private val creditViewData by lazy {
        CreditViewData(
            externalFinancierSupported = true,
            totalCreditLimit = "totalCreditLimit",
            totalAvailableCreditLimit = "-50000",
            totalOutstandingAmount = "10000",
            principalOutstandingAmount = "principalOutstandingAmount",
            interestOutstandingAmount = "interestOutstandingAmount",
            overdueInterestOutstandingAmount = "overdueInterestOutstandingAmount",
            penaltyOutstandingAmount = "penaltyOutstandingAmount"
        )
    }

    private val infoViewData by lazy {
        InfoViewData(
            totalPurchaseAmount = "totalPurchaseAmount",
            totalPaymentAmount = "totalPaymentAmount",
            undeliveredInvoiceAmount = "undeliveredInvoiceAmount"
        )
    }
    private val overdueViewData by lazy {
        OverdueViewData(
            totalOverdueLimit = "totalOverdueLimit",
            totalOverdueAmount = "totalOverdueAmount",
            minPaymentAmount = "minPaymentAmount",
            minPaymentDueDate = 78
        )
    }
    val creditSummaryViewData by lazy {
        CreditSummaryViewData(
            creditViewData,
            overdueViewData,
            infoViewData
        )
    }
    val creditLineViewData by lazy {
        CreditLineViewData(
            belongsToGapl = true,
            lenderViewName = "lenderViewName",
            creditLimit = "creditLimit",
            availableCreditLimit = "availableCreditLimit",
            totalOutstandingAmount = "totalOutstandingAmount",
            principalOutstandingAmount = "principalOutstandingAmount",
            interestOutstandingAmount = "interestOutstandingAmount",
            overdueInterestOutstandingAmount = "overdueInterestOutstandingAmount",
            penaltyOutstandingAmount = "penaltyOutstandingAmount",
            advanceAmount = "advanceAmount"
        )
    }
    val transactionViewData by lazy {
        TransactionViewData(
            ledgerId = "ledgerId",
            type = "type",
            date = 67384543,
            amount = "amount",
            erpId = "erpId",
            locusId = "locusId",
            creditNoteReason = "creditNoteReason",
            paymentMode = "paymentMode"
        )
    }
    private val dbaApp by lazy {
        LedgerParentApp.DBA(
            ledgerCallBack = object : LedgerCallbacks {
                override fun onClickPayNow(
                    creditSummaryViewData: CreditSummaryViewData?
                ) = Unit

                override fun onClickDownloadInvoice(
                    invoiceDetailDataViewData: InvoiceDetailDataViewData?
                ) = Unit

                override fun onPaymentOptionsClick(
                    creditSummaryViewData: CreditSummaryViewData?,
                    resultLauncher: ActivityResultLauncher<Intent?>
                ) = Unit
            }
        )
    }
    private val aimsApp by lazy { LedgerParentApp.AIMS() }

    fun initDBA(context: Context) = LedgerSDK.init(
        context,
        dbaApp
    )

    fun initAIMS(context: Context) = LedgerSDK.init(
        context,
        aimsApp
    )
}
