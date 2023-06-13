package lib.dehaat.ledger.presentation.model.revamp.transactions

import lib.dehaat.ledger.presentation.ledger.ui.component.TransactionType

data class TransactionViewDataV2(
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
	val invoiceStatus: String?,
	val statusVariable: String?,
	val totalInvoiceAmount: Double?,
	val totalInterestCharged: Double?,
	val totalRemainingAmount: Double?
) {

	companion object {
		fun monthlySeparator(month: String)  = TransactionViewDataV2(
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
			prepaidAmount = null,
			invoiceStatus = null,
			statusVariable = null,
			totalInvoiceAmount= null,
			totalInterestCharged = null,
			totalRemainingAmount = null
		)
	}
}
