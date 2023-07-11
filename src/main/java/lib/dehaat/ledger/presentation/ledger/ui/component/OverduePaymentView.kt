package lib.dehaat.ledger.presentation.ledger.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lib.dehaat.ledger.R
import lib.dehaat.ledger.datasource.DummyDataSource.summaryViewData
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.annotations.AgeingBannerPriority
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.resources.Error90
import lib.dehaat.ledger.resources.LedgerTheme
import lib.dehaat.ledger.resources.Neutral10
import lib.dehaat.ledger.resources.TextWhite
import lib.dehaat.ledger.resources.textParagraphT2
import lib.dehaat.ledger.resources.textSubHeadingS3

@Preview
@Composable
private fun OverduePaymentPreview() = LedgerTheme {
	OverduePaymentView(
		creditLineSubStatus = LedgerConstants.MISCELLANEOUS,
		agedOutstandingAmount = "100.00",
		repaymentUnblockAmount = "400.00",
		summaryViewData = summaryViewData
	)
}

@Composable
fun OverduePaymentView(
	creditLineSubStatus: String,
	agedOutstandingAmount: String,
	repaymentUnblockAmount: String,
	summaryViewData: SummaryViewData
) = when (creditLineSubStatus) {
	LedgerConstants.MISCELLANEOUS -> stringResource(id = R.string.ledger_credit_line_status_miscellaneous)
	LedgerConstants.AGED_OUTSTANDING -> getOutStandingSubtitle(
		summaryViewData,
		summaryViewData.agedOverdueAmount ?: agedOutstandingAmount
	)

	LedgerConstants.REPAYMENT_FREQUENCY -> stringResource(
		id = R.string.ledger_credit_line_status_repayment_frequency,
		repaymentUnblockAmount
	)

	LedgerConstants.ON_BOARDING_POD -> stringResource(id = R.string.ledger_credit_line_status_onboarding_pod)
	else -> null
}?.let { label ->
	Row(
		Modifier
			.fillMaxWidth()
			.background(Error90),
		verticalAlignment = Alignment.CenterVertically
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.padding(horizontal = 20.dp, vertical = 16.dp)
		) {
			Text(
				modifier = Modifier.fillMaxWidth(),
				text = if (creditLineSubStatus == LedgerConstants.AGED_OUTSTANDING) getOutstandingTitle(
					summaryViewData
				) else stringResource(R.string.your_ordering_is_blocked),
				style = textSubHeadingS3(Neutral10)
			)
			VerticalSpacer(height = 4.dp)
			Text(
				modifier = Modifier.fillMaxWidth(),
				text = label,
				style = textParagraphT2(TextWhite)
			)
		}

		Image(
			painter = painterResource(id = R.drawable.ic_overdue_lock),
			contentDescription = null,
			contentScale = ContentScale.Crop
		)
	}
}

@Composable
fun getOutStandingSubtitle(
	summaryViewData: SummaryViewData,
	amount: String
): String =
	when (summaryViewData.ageingBannerPriority) {
		AgeingBannerPriority.PRIORITY_0 -> stringResource(R.string.defaulted_bank_payments, amount)
		AgeingBannerPriority.PRIORITY_1 -> stringResource(
			R.string.pay_amount_to_avoid_negative_impact, amount
		)

		AgeingBannerPriority.PRIORITY_2 -> stringResource(
			R.string.ordering_blocked_pay_to_unlock, amount
		)

		AgeingBannerPriority.PRIORITY_3 -> stringResource(
			R.string.pay_s_amount_to_immediately_unblock, amount
		)

		else -> stringResource(R.string.as_you_haved_cleared_outstanding_s_pay_immediately, amount)
	}

@Composable
private fun getOutstandingTitle(summaryViewData: SummaryViewData?) =
	when (summaryViewData?.ageingBannerPriority) {
		AgeingBannerPriority.PRIORITY_0 -> stringResource(R.string.cibil_score_decreased)
		AgeingBannerPriority.PRIORITY_1 -> stringResource(R.string.cibil_score_will_decrease)
		AgeingBannerPriority.PRIORITY_2 -> stringResource(R.string.penalty_will_be_charged)
		AgeingBannerPriority.PRIORITY_3 -> stringResource(R.string.your_ordering_is_blocked_ledger)
		else -> stringResource(R.string.your_ordering_is_blocked)
	}
