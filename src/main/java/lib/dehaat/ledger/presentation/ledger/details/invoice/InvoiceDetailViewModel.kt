package lib.dehaat.ledger.presentation.ledger.details.invoice

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import com.dehaat.androidbase.helper.orFalse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetInvoiceDetailUseCase
import lib.dehaat.ledger.domain.usecases.InvoiceDownloadUseCase
import lib.dehaat.ledger.entities.detail.invoice.InvoiceDetailDataEntity
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LEDGER_ID
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LMS_ACTIVATED
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_SOURCE
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.details.invoice.state.InvoiceDetailViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.invoicedownload.DownloadSource
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.presentation.model.invoicedownload.ProgressData
import lib.dehaat.ledger.presentation.model.transactions.TransactionViewData
import lib.dehaat.ledger.presentation.model.widgetinvoicelist.WidgetInvoiceViewData
import lib.dehaat.ledger.util.DownloadFileUtil
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailViewModel @Inject constructor(
	private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase,
	private val invoiceDownloadUseCase: InvoiceDownloadUseCase,
	private val mapper: LedgerViewDataMapper,
	private val downloadFileUtil: DownloadFileUtil,
	savedStateHandle: SavedStateHandle
) : BaseViewModel() {

	private val ledgerId by lazy {
		savedStateHandle.get<String>(KEY_LEDGER_ID) ?: throw Exception(
			"Ledger id should not null"
		)
	}

	val erpId by lazy { savedStateHandle.get<String>(KEY_ERP_ID) }
	val source by lazy { savedStateHandle.get<String>(KEY_SOURCE) ?: "" }

	private val lmsActivated: Boolean by lazy {
        savedStateHandle.get<Boolean>(KEY_LMS_ACTIVATED).orFalse()
    }

	private val _uiEvent = MutableSharedFlow<UiEvent>()
	val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

	private val invoiceDownloadData = InvoiceDownloadData()

	private val viewModelState = MutableStateFlow(InvoiceDetailViewModelState())
	val uiState = viewModelState
		.map { it.toUiState() }
		.stateIn(
			viewModelScope,
			SharingStarted.Eagerly,
			viewModelState.value.toUiState()
		)

	init {
		getInvoiceDetailFromServer()
	}

	fun isLmsActivated() = lmsActivated



	private fun getInvoiceDetailFromServer() {
		callInViewModelScope {
			callingAPI()
			val response = getInvoiceDetailUseCase.getInvoiceDetail(ledgerId)
			calledAPI()
			processInvoiceDetailResponse(response)
		}
	}

	private fun processInvoiceDetailResponse(result: APIResultEntity<InvoiceDetailDataEntity?>) {
		result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) { creditSummaryEntity ->
			val invoiceDetailViewData = mapper.toInvoiceDetailDataViewData(creditSummaryEntity)
			viewModelState.update {
				it.copy(isLoading = false, invoiceDetailDataViewData = invoiceDetailViewData)
			}
		}
	}

	private fun sendShowSnackBarEvent(message: String) {
		updateAPIFailure()
		viewModelScope.launch {
			_uiEvent.emit(UiEvent.ShowSnackBar(message))
		}
	}

	private fun updateAPIFailure() = viewModelState.update {
		it.copy(
			isError = true,
			isLoading = false
		)
	}

	fun downloadInvoice(
		file: File,
		invoiceDownloadStatus: (InvoiceDownloadData) -> Unit
	) = callInViewModelScope {
		invoiceDownloadData.partnerId = ledgerId
		val identityId = when (source) {
			DownloadSource.SAP -> erpId?.substringAfterLast('$')
			DownloadSource.ODOO -> erpId
			else -> null
		}
		identityId?.let {
			invoiceDownloadUseCase.getDownloadInvoice(
				it,
				source,
				file,
				invoiceDownloadData,
				invoiceDownloadStatus,
				::updateProgressDialog,
				::sendShowSnackBarEvent,
				::updateDownloadPathAndProgress,
				::downloadFile
			)
		}
	}

	fun updateProgressDialog(show: Boolean) = viewModelState.update {
		it.copy(isLoading = show)
	}

	private fun downloadFile(
		identityId: String,
		file: File,
		invoiceDownloadStatus: (InvoiceDownloadData) -> Unit
	) = callInViewModelScope {
		updateProgressDialog(true)
		downloadFileUtil.downloadFile(
			File(file, "$identityId.pdf"),
			identityId,
			LedgerSDK.bucket
		)?.setTransferListener(
			object : TransferListener {
				override fun onStateChanged(id: Int, state: TransferState?) {
					if (state == TransferState.COMPLETED) {
						updateProgressDialog(false)
						updateDownloadPathAndProgress(file, identityId)
						invoiceDownloadStatus(invoiceDownloadData)
					}
				}

				override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
					invoiceDownloadData.progressData =
						ProgressData(bytesCurrent.toInt(), bytesTotal.toInt())
					invoiceDownloadStatus(invoiceDownloadData)
				}

				override fun onError(id: Int, ex: Exception?) {
					ex?.printStackTrace()
					invoiceDownloadData.isFailed = true
					updateDownloadPathAndProgress(file, identityId)
					invoiceDownloadStatus(invoiceDownloadData)
					updateProgressDialog(false)
				}
			}
		)
	}

	private fun calledAPI() {
		viewModelState.update {
			it.copy(isLoading = false)
		}
	}

	private fun callingAPI() {
		viewModelState.update {
			it.copy(isLoading = true)
		}
	}

	private fun updateDownloadPathAndProgress(file: File, id: String) = invoiceDownloadData.apply {
		filePath = "${file.path}/${id.substringAfterLast('$')}.pdf"
		progressData = ProgressData()
		invoiceId = id.substringAfterLast('$')
	}

	companion object {
		private const val KEY_ERP_ID = "KEY_ERP_ID"
		fun getArgs(data: TransactionViewData, isLMSActivated: Boolean) = Bundle().apply {
			putString(KEY_LEDGER_ID, data.ledgerId)
			putString(KEY_ERP_ID, data.erpId)
			putString(KEY_SOURCE, data.source)
            putBoolean(KEY_LMS_ACTIVATED, isLMSActivated)
		}

		fun getArgs(data: WidgetInvoiceViewData) = Bundle().apply {
			putString(KEY_LEDGER_ID, data.ledgerId)
			putString(KEY_ERP_ID, data.erpId)
			putString(KEY_SOURCE, data.source)
		}
	}
}
