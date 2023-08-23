package lib.dehaat.ledger.presentation.ledger.details.invoice.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehaat.androidbase.helper.showToast
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.common.uicomponent.CommonContainer
import lib.dehaat.ledger.presentation.common.uicomponent.HorizontalSpacer
import lib.dehaat.ledger.presentation.common.uicomponent.VerticalSpacer
import lib.dehaat.ledger.presentation.ledger.annotations.InvoiceStatus
import lib.dehaat.ledger.presentation.ledger.components.NoDataFound
import lib.dehaat.ledger.presentation.ledger.components.ShowProgressDialog
import lib.dehaat.ledger.presentation.ledger.details.invoice.RevampInvoiceDetailViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.ledger.transactions.ui.component.InvoiceStatusView
import lib.dehaat.ledger.presentation.ledger.ui.component.FullyPaidTag
import lib.dehaat.ledger.presentation.ledger.ui.component.ProductDetailsScreen
import lib.dehaat.ledger.presentation.ledger.ui.component.RevampKeyValuePair
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.presentation.model.revamp.invoice.CreditNoteViewData
import lib.dehaat.ledger.presentation.model.revamp.invoice.InterestOverdueViewData
import lib.dehaat.ledger.presentation.model.revamp.invoice.PrepaidAndCreditInfoViewDataV2
import lib.dehaat.ledger.presentation.model.revamp.invoice.ProductsInfoViewDataV2
import lib.dehaat.ledger.presentation.model.revamp.invoice.SummaryViewDataV2
import lib.dehaat.ledger.resources.Background
import lib.dehaat.ledger.resources.ColorF6F6F6
import lib.dehaat.ledger.resources.Error100
import lib.dehaat.ledger.resources.Error5
import lib.dehaat.ledger.resources.Error90
import lib.dehaat.ledger.resources.Neutral60
import lib.dehaat.ledger.resources.Neutral80
import lib.dehaat.ledger.resources.Neutral90
import lib.dehaat.ledger.resources.Primary10
import lib.dehaat.ledger.resources.SeaGreen100
import lib.dehaat.ledger.resources.SeaGreen20
import lib.dehaat.ledger.resources.Secondary10
import lib.dehaat.ledger.resources.Secondary120
import lib.dehaat.ledger.resources.mediumShape
import lib.dehaat.ledger.resources.smallShape
import lib.dehaat.ledger.resources.textButtonB2
import lib.dehaat.ledger.resources.textCaptionCP1
import lib.dehaat.ledger.resources.textMedium12Sp
import lib.dehaat.ledger.resources.textParagraphT1Highlight
import lib.dehaat.ledger.resources.textParagraphT2
import lib.dehaat.ledger.resources.textParagraphT2Highlight
import lib.dehaat.ledger.resources.textSemiBold12Sp
import lib.dehaat.ledger.resources.textSemiBold14Sp
import lib.dehaat.ledger.resources.themes.LedgerColors
import lib.dehaat.ledger.util.GifImage
import lib.dehaat.ledger.util.HandleAPIErrors
import lib.dehaat.ledger.util.clickableWithCorners
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.util.toDateMonthYear

@Composable
fun RevampInvoiceDetailScreen(
	isDCFinanced: Boolean,
	viewModel: RevampInvoiceDetailViewModel = hiltViewModel(),
	ledgerColors: LedgerColors,
	onDownloadInvoiceClick: (InvoiceDownloadData) -> Unit,
	onError: (Exception) -> Unit,
	onBackPress: () -> Unit
) {
	HandleAPIErrors(viewModel.
	uiEvent)
	val uiState by viewModel.uiState.collectAsState()
	val context = LocalContext.current
	CommonContainer(
		title = stringResource(id = R.string.invoice_details),
		onBackPress = onBackPress,
		backgroundColor = Background,
		ledgerColors = ledgerColors,
		bottomBar = {
			AnimatedVisibility(visible = false) {}
		}
	) {
		when (uiState.state) {
			UIState.SUCCESS -> {
				uiState.invoiceDetailsViewData?.let {
					InvoiceDetailScreen(
						isDCFinanced = isDCFinanced,
						it.prepaidAndCreditInfoViewDataV2,
						it.summary,
						it.creditNotes,
						it.productsInfo,
						it.interestOverdueViewData
					) {
						LedgerSDK.getFile(context)?.let { file ->
							viewModel.downloadInvoice(
								file,
								onDownloadInvoiceClick
							)
						} ?: run {
							context.showToast(R.string.tech_problem)
							LedgerSDK.currentApp.ledgerCallBack.exceptionHandler(
								Exception("Unable to create file")
							)
						}
					}
				} ?: NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
			}

			UIState.LOADING -> {
				ShowProgressDialog(ledgerColors) {
					viewModel.updateProgressDialog(false)
				}
			}

			is UIState.ERROR -> {
				NoDataFound((uiState.state as? UIState.ERROR)?.message, onError)
			}
		}
	}
}

@Composable
private fun InvoiceDetailScreen(
	isDCFinanced: Boolean,
	prepaidAndCreditInfoViewDataV2: PrepaidAndCreditInfoViewDataV2?,
	summary: SummaryViewDataV2,
	creditNotes: List<CreditNoteViewData>,
	productsInfo: ProductsInfoViewDataV2,
	interestOverdueViewData: InterestOverdueViewData?,
	onDownloadInvoiceClick: () -> Unit
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.verticalScroll(rememberScrollState())
) {

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(Color.White)
			.padding(horizontal = 20.dp)
	) {

		VerticalSpacer(height = 24.dp)

		if (showPrepaidTag(summary, prepaidAndCreditInfoViewDataV2)) {
			FullyPaidTag(modifier = Modifier.padding(top = 22.dp))
			Spacer(modifier = Modifier.padding(top = 10.dp))
		} else {
			InvoiceStatusView(
				status = interestOverdueViewData?.invoiceStatus,
				statusVariable = interestOverdueViewData?.statusVariable
			)
		}

		VerticalSpacer(height = 12.dp)
		RevampKeyValuePair(
			pair = Pair(
				stringResource(id = R.string.invoice_amount),
				summary.invoiceAmount.getAmountInRupees()
			),
			style = Pair(
				textParagraphT2Highlight(Neutral90),
				textButtonB2(Neutral90)
			)
		)

		if (interestOverdueViewData?.invoiceStatus != InvoiceStatus.INTEREST_START_DATE && (isDCFinanced || summary.showTotalInterestCharged)) {
			VerticalSpacer(height = 12.dp)
			InterestAmount(summary.totalInterestCharged)
		}

		VerticalSpacer(height = 12.dp)

		summary.totalOutstandingAmount?.let {
			if (it.toDoubleOrNull() != 0.0) {
				VerticalSpacer(height = 8.dp)
				RevampKeyValuePair(
					pair = Pair(
						stringResource(id = R.string.outstanding_amount),
						it.getAmountInRupees()
					),
					style = Pair(
						textParagraphT2Highlight(Error100),
						textButtonB2(Error100)
					)
				)
				VerticalSpacer(height = 12.dp)
			}
		}

		VerticalSpacer(height = 12.dp)
		RevampKeyValuePair(
			pair = Pair(
				stringResource(id = R.string.invoice_id),
				summary.invoiceId
			),
			style = Pair(
				textParagraphT2Highlight(Neutral80),
				textParagraphT2Highlight(Neutral90)
			)
		)

		VerticalSpacer(height = 12.dp)
		RevampKeyValuePair(
			pair = Pair(
				stringResource(id = R.string.ledger_invoice_date),
				summary.invoiceDate.toDateMonthYear()
			),
			style = Pair(
				textParagraphT2Highlight(Neutral80),
				textButtonB2(Neutral90)
			)
		)

		summary.interestStartDate?.let {
			VerticalSpacer(height = 12.dp)
			RevampKeyValuePair(
				pair = Pair(
					stringResource(id = R.string.interest_start_date),
					it.toDateMonthYear()
				),
				style = Pair(
					textParagraphT2Highlight(Neutral80),
					textButtonB2(Neutral90)
				)
			)
		}

		summary.penaltyAmount?.let {
			VerticalSpacer(height = 12.dp)
			RevampKeyValuePair(
				pair = Pair(
					stringResource(R.string.ledger_penalty_amount),
					it
				),
				style = Pair(
					textParagraphT2Highlight(Error100),
					textButtonB2(Error100)
				)
			)

			VerticalSpacer(height = 8.dp)

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.background(color = Error5, shape = mediumShape())
					.padding(8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				GifImage(
					modifier = Modifier.size(72.dp),
					drawable = R.drawable.calendar_animation,
					contentDescription = ""
				)

				HorizontalSpacer(width = 8.dp)

				Text(
					text = stringResource(
						R.string.ledger_penalty_amount_amount_description,
						summary.invoiceAge,
						it
					),
					style = textParagraphT2(
						Error90
					)
				)
			}
		}

		if (summary.showProcessingLabel) {
			VerticalSpacer(height = 8.dp)

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.background(Secondary10, shape = RoundedCornerShape(8.dp))
					.padding(8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				GifImage(
					modifier = Modifier.size(72.dp),
					drawable = R.drawable.savings_piggy,
					contentDescription = ""
				)

				HorizontalSpacer(8.dp)

				Text(
					text = stringResource(R.string.ledger_invoice_in_processing),
					style = textParagraphT2Highlight(Secondary120)
				)
			}
		}

		VerticalSpacer(height = 8.dp)
	}

	if (interestOverdueViewData?.interestPerDay != null) {
		Column(
			Modifier
				.fillMaxWidth()
				.background(Color.White)
		) {
			Text(
				text = stringResource(
					R.string.interest_rate_s_per_day, interestOverdueViewData.interestPerDay
				),
				modifier = Modifier
					.padding(end = 20.dp)
					.clip(smallShape())
					.background(ColorF6F6F6)
					.padding(horizontal = 8.dp, vertical = 4.dp)
					.align(Alignment.End),
				style = textSemiBold12Sp(Neutral80)
			)
			VerticalSpacer(height = 12.dp)
		}
	}

	if (creditNotes.isNotEmpty()) {
		VerticalSpacer(height = 8.dp)
		CreditNoteDetails(creditNotes)

	}
	VerticalSpacer(height = 8.dp)

	ProductDetailsScreen(productsInfo)

	DownloadInvoiceButton(onDownloadInvoiceClick)
}

@Composable
private fun InterestAmount(interestBeingCharged: String) {
	Row(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Column {
			Text(
				text = stringResource(R.string.ledger_interest_amount),
				style = textParagraphT2Highlight(Neutral90)
			)
			Text(
				text = stringResource(R.string.interest_summation),
				style = textMedium12Sp(Neutral60)
			)
		}

		Text(
			text = interestBeingCharged,
			style = textButtonB2(Neutral90)
		)
	}
}

@Composable
private fun CreditNoteDetails(
	creditNotes: List<CreditNoteViewData>
) = Column(
	modifier = Modifier.background(Color.White)
) {
	VerticalSpacer(height = 20.dp)
	Text(
		modifier = Modifier.padding(horizontal = 20.dp),
		text = stringResource(id = R.string.credit_note_received)
	)

	VerticalSpacer(height = 12.dp)

	Divider()

	creditNotes.forEach {
		CreditNoteCard(it)
	}
}

@Composable
private fun CreditNoteCard(
	creditNote: CreditNoteViewData
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.padding(horizontal = 20.dp)
) {
	Spacer(modifier = Modifier.height(12.dp))
	Row(modifier = Modifier.fillMaxWidth()) {
		Image(
			modifier = Modifier
				.height(32.dp)
				.width(32.dp),
			painter = painterResource(id = R.drawable.ic_transactions_credit_note),
			contentDescription = stringResource(id = R.string.accessibility_icon)
		)
		Spacer(modifier = Modifier.width(8.dp))
		Column {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = stringResource(
						id = R.string.credit_note_ledger,
						creditNote.creditNoteType
					),
					style = textParagraphT1Highlight(Neutral80)
				)
				Text(
					text = creditNote.creditNoteAmount.getAmountInRupees(),
					style = textParagraphT1Highlight(Neutral80)
				)
			}
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				text = creditNote.creditNoteDate.toDateMonthYear(),
				style = textCaptionCP1(Neutral60)
			)
		}
	}
	Spacer(modifier = Modifier.height(16.dp))
	Divider()
}

@Composable
fun DownloadInvoiceButton(
	onClick: () -> Unit
) = Column(
	modifier = Modifier
		.fillMaxWidth()
		.background(Color.White),
	verticalArrangement = Arrangement.Center,
	horizontalAlignment = Alignment.CenterHorizontally
) {
	Row(
		modifier = Modifier
			.padding(top = 16.dp)
			.clickableWithCorners(
				borderSize = 8.dp,
				onClick = onClick,
				backgroundColor = Primary10
			)
			.border(
				width = 1.dp,
				color = SeaGreen20,
				shape = mediumShape()
			)
			.padding(vertical = 9.dp, horizontal = 16.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_invoice_download),
			contentDescription = stringResource(id = R.string.accessibility_icon),
			tint = SeaGreen100
		)
		HorizontalSpacer(width = 6.dp)
		Text(
			text = stringResource(id = R.string.download_invoice),
			style = textSemiBold14Sp(SeaGreen100)
		)
		HorizontalSpacer(width = 8.dp)
	}

	VerticalSpacer(height = 16.dp)
}

fun showPrepaidTag(
	summary: SummaryViewDataV2,
	prepaidAndCreditInfo: PrepaidAndCreditInfoViewDataV2?
): Boolean {
	val invoiceAmount = summary.invoiceAmount
	return invoiceAmount != null && invoiceAmount == prepaidAndCreditInfo?.prepaidAmount
}