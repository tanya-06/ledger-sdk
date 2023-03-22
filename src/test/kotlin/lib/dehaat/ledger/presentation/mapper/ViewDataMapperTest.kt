package lib.dehaat.ledger.presentation.mapper

import com.google.common.truth.Truth.assertThat
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ViewDataMapperTest {

    private lateinit var mapper: ViewDataMapper

    @BeforeEach
    fun setUp() {
        mapper = ViewDataMapper()
    }

    @Test
    fun `CALLING toTransactionSummaryViewData SHOULD return fields WITH rounded amounts in Indian Currency`() {
        val purchaseAmount = "7.49"
        val expectedPurchaseAmount = "₹7"

        val paymentAmount = "-6.50"
        val expectedPaymentAmount = "- ₹6"

        val interestAmount = "8.51"
        val expectedInterestAmount = "₹9"

        val transaction = TransactionSummaryEntity(
            purchaseAmount, paymentAmount, interestAmount
        )
        val viewData = mapper.toTransactionSummaryViewData(transaction)

        assertThat(viewData.interestAmount).isEqualTo(expectedInterestAmount)
        assertThat(viewData.purchaseAmount).isEqualTo(expectedPurchaseAmount)
        assertThat(viewData.paymentAmount).isEqualTo(expectedPaymentAmount)
    }
}
