package lib.dehaat.ledger.data

import lib.dehaat.ledger.data.source.ILedgerDataSource
import lib.dehaat.ledger.domain.ILedgerRepository
import javax.inject.Inject

class LedgerRepository @Inject constructor(private val networkSource: ILedgerDataSource) :
    ILedgerRepository {

    override suspend fun getCreditSummary(partnerId: String) =
        networkSource.getCreditSummary(partnerId)

    override suspend fun getTransactionSummary(
        partnerId: String
    ) = networkSource.getTransactionSummary(partnerId)

    override suspend fun getTransactions(
        partnerId: String,
        limit: Int,
        offset: Int,
        onlyPenaltyInvoices: Boolean,
        fromDate: Long?,
        toDate: Long?,
    ) = networkSource.getTransactions(
        partnerId,
        limit,
        offset,
        onlyPenaltyInvoices,
        fromDate,
        toDate
    )

    override suspend fun getCreditLines(
        partnerId: String
    ) = networkSource.getCreditLines(
        partnerId
    )

    override suspend fun getInvoiceDetail(
        ledgerId: String
    ) = networkSource.getInvoiceDetail(
        ledgerId
    )

    override suspend fun getPaymentDetail(
        ledgerId: String
    ) = networkSource.getPaymentDetail(
        ledgerId
    )

    override suspend fun getCreditNoteDetail(
        ledgerId: String
    ) = networkSource.getCreditNoteDetail(
        ledgerId
    )

}