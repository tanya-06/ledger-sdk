package lib.dehaat.ledger.presentation.ledger.details.invoice

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetInvoiceDetailUseCase
import lib.dehaat.ledger.domain.usecases.GetInvoiceDownloadUseCase
import lib.dehaat.ledger.entities.revamp.invoice.InvoiceDataEntity
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.revamp.state.invoice.InvoiceDetailsViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.presentation.model.invoicedownload.ProgressData
import lib.dehaat.ledger.util.DownloadFileUtil
import lib.dehaat.ledger.util.FileUtils
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar
import java.io.File

@HiltViewModel
class RevampInvoiceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase,
    private val mapper: LedgerViewDataMapper,
    private val downloadFileUtil: DownloadFileUtil,
    private val getInvoiceDownloadUseCase: GetInvoiceDownloadUseCase,
) : BaseViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val invoiceDownloadData = InvoiceDownloadData()

    val erpId by lazy { savedStateHandle.get<String>(KEY_ERP_ID) }
    val ledgerId by lazy { savedStateHandle.get<String>(LedgerConstants.KEY_LEDGER_ID) ?: "" }
    val source by lazy { savedStateHandle.get<String>(LedgerConstants.KEY_SOURCE) ?: "" }

    private val viewModelState = MutableStateFlow(InvoiceDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUIState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUIState()
        )

    init {
        getInvoiceDetailFromServer()
    }

    private fun getInvoiceDetailFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getInvoiceDetailUseCase.getInvoiceDetails(ledgerId)
            calledAPI()
            processInvoiceDetailResponse(response)
        }
    }

    private fun processInvoiceDetailResponse(result: APIResultEntity<InvoiceDataEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendFailureEvent) { entity ->
            entity?.let { invoiceDataEntity ->
                val invoiceDetailsViewData = mapper.toInvoiceDetailsViewData(invoiceDataEntity)
                viewModelState.update {
                    it.copy(
                        isSuccess = true,
                        invoiceDetailsViewData = invoiceDetailsViewData
                    )
                }
            }
        }
    }

    private fun calledAPI() = updateProgressDialog(false)

    private fun callingAPI() = updateProgressDialog(true)

    fun updateProgressDialog(show: Boolean) = viewModelState.update {
        it.copy(isLoading = show)
    }

    private fun sendFailureEvent(message: String) {
        viewModelState.update {
            it.copy(
                isError = true,
                errorMessage = message
            )
        }
    }

    fun downloadInvoice(
        file: File,
        invoiceDownloadStatus: (InvoiceDownloadData) -> Unit
    ) = callInViewModelScope {
        invoiceDownloadData.partnerId = ledgerId
        val identityId = when (source) {
            "SAP" -> erpId?.substringAfterLast('$')
            "ODOO" -> erpId
            else -> null
        }
        identityId?.let {
            getDownloadInvoice(it, source, file, invoiceDownloadStatus)
        }
    }

    private fun getDownloadInvoice(
        identityId: String,
        source: String,
        file: File,
        invoiceDownloadStatus: (InvoiceDownloadData) -> Unit
    ) = callInViewModelScope {
        updateProgressDialog(true)
        val result = getInvoiceDownloadUseCase.invoke(identityId, source)
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) {
            it?.let { invoiceDownloadDataEntity ->
                when (invoiceDownloadDataEntity.source) {
                    "SAP" -> {
                        updateProgressDialog(false)
                        FileUtils.getFileFromBase64(
                            base64 = invoiceDownloadDataEntity.pdf.orEmpty(),
                            fileType = invoiceDownloadDataEntity.docType,
                            fileName = identityId,
                            dir = file
                        )?.let {
                            updateDownloadPathAndProgress(file, identityId)
                            invoiceDownloadStatus(invoiceDownloadData)
                        } ?: kotlin.run {
                            invoiceDownloadData.isFailed = true
                            invoiceDownloadStatus(invoiceDownloadData)
                        }
                    }
                    "ODOO" -> {
                        updateProgressDialog(false)
                        invoiceDownloadDataEntity.fileName
                            ?: return@processAPIResponseWithFailureSnackBar
                        downloadFile(
                            invoiceDownloadDataEntity.fileName,
                            file,
                            invoiceDownloadStatus
                        )
                    }
                    else -> {
                        invoiceDownloadData.isFailed = true
                        invoiceDownloadStatus(invoiceDownloadData)
                        updateProgressDialog(false)
                    }
                }
            }
        }
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

    private fun updateDownloadPathAndProgress(file: File, id: String) = invoiceDownloadData.apply {
        filePath = "${file.path}/${id.substringAfterLast('$')}.pdf"
        progressData = ProgressData()
        invoiceId = id.substringAfterLast('$')
    }

    private fun sendShowSnackBarEvent(message: String) {
        updateAPIFailure()
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackbar(message))
        }
    }

    private fun updateAPIFailure() = viewModelState.update {
        it.copy(
            isError = true,
            isLoading = false
        )
    }

    companion object {
        private const val KEY_ERP_ID = "KEY_ERP_ID"
        fun getBundle(ledgerId: String, source: String, erpId: String?) = Bundle().apply {
            putString(LedgerConstants.KEY_LEDGER_ID, ledgerId)
            putString(KEY_ERP_ID, erpId)
            putString(LedgerConstants.KEY_SOURCE, source)
        }
    }
}
