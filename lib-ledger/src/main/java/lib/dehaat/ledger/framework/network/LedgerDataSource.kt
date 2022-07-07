package lib.dehaat.ledger.framework.network

import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.coroutine.IDispatchers
import com.dehaat.androidbase.network.api.makeAPICall
import lib.dehaat.ledger.data.source.ILedgerDataSource
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.framework.mapper.LedgerFrameworkMapper
import retrofit2.Response
import javax.inject.Inject

class LedgerDataSource @Inject constructor(
    private val dispatcher: IDispatchers,
    private val apiService: LedgerAPIService,
    private val mapper: LedgerFrameworkMapper
) : ILedgerDataSource {

    override suspend fun getCreditSummary(partnerId: String) = callAPI(
        dispatcher,
        { apiService.getCreditSummary(partnerId = partnerId) }) {
        it?.data?.let { data -> mapper.toCreditSummaryDataEntity(data) }
    }

    override suspend fun getTransactionSummary(
        partnerId: String
    ): APIResultEntity<TransactionSummaryEntity?> = callAPI(
        dispatcher,
        { apiService.getTransactionSummary(partnerId) }
    ) {
        it?.transactionDetailData?.let { data -> mapper.toTransactionSummaryDataEntity(data) }
    }

    override suspend fun getTransactions(
        partnerId: String,
        limit: Int,
        offset: Int,
        onlyPenaltyInvoices: Boolean,
        fromDate: Long?,
        toDate: Long?,
    ) = callAPI(
        dispatcher,
        {
            apiService.getTransactions(
                partnerId = partnerId,
                fromDate = fromDate,
                toDate = toDate,
                onlyPenaltyInvoices = onlyPenaltyInvoices,
                limit = limit,
                offset = offset
            )
        }) {
        it?.transactionsData?.let { data -> mapper.toTransactionsDataEntity(data) } ?: emptyList()
    }

    override suspend fun getCreditLines(
        partnerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getCreditLines(partnerId = partnerId) }) {
        it?.creditLineData?.let { data -> mapper.toCreditLineDataEntity(data) } ?: emptyList()
    }

    override suspend fun getInvoiceDetail(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getInvoiceDetail(ledgerId) }
    ) {
        it?.invoiceDetailData?.let { data -> mapper.toInvoiceDetailDataEntity(data) }
    }

    override suspend fun getPaymentDetail(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getPaymentDetail(ledgerId) }
    ) {
        it?.paymentDetailData?.let { data -> mapper.toPaymentDetailDataEntity(data) }
    }

    override suspend fun getCreditNoteDetail(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getCreditNoteDetail(ledgerId) }
    ) {
        it?.creditNoteDetailData?.let { data -> mapper.toCreditNoteDetailDataEntity(data) }
    }

    private suspend fun <D, C> callAPI(
        dispatchers: IDispatchers,
        apiCall: suspend () -> Response<D>,
        parse: (D?) -> C
    ): APIResultEntity<C> {
        return makeAPICall(dispatchers.io, {
            apiCall.invoke()
        }, parse)
    }
}
