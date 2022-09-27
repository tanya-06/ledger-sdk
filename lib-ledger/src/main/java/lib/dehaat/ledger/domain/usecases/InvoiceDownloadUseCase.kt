package lib.dehaat.ledger.domain.usecases

import java.io.File
import javax.inject.Inject
import lib.dehaat.ledger.presentation.model.invoicedownload.DownloadSource
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.util.FileUtils
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar

class InvoiceDownloadUseCase @Inject constructor(
	private val getInvoiceDownloadUseCase: GetInvoiceDownloadUseCase
) {

	suspend fun getDownloadInvoice(
		identityId: String,
		source: String,
		file: File,
		invoiceDownloadData: InvoiceDownloadData,
		invoiceDownloadStatus: (InvoiceDownloadData) -> Unit,
		updateProgressDialog: (Boolean) -> Unit,
		sendShowSnackBarEvent: (String) -> Unit,
		updateDownloadPathAndProgress: (File, String) -> Unit,
		downloadFile: (String, File, (InvoiceDownloadData) -> Unit) -> Unit
	) {
		updateProgressDialog(true)
		val result = getInvoiceDownloadUseCase.invoke(identityId, source)
		result.processAPIResponseWithFailureSnackBar(
			onFailure = {
				sendShowSnackBarEvent(it)
			}
		) { invoiceDownloadDataEntity ->
			when (invoiceDownloadDataEntity.source) {
				DownloadSource.SAP -> {
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
				DownloadSource.ODOO -> {
					updateProgressDialog(false)
					invoiceDownloadDataEntity.fileName?.let {
						downloadFile(
							it,
							file,
							invoiceDownloadStatus
						)
					} ?: kotlin.run {
						invoiceDownloadData.isFailed = true
						invoiceDownloadStatus(invoiceDownloadData)
						updateProgressDialog(false)
						return@processAPIResponseWithFailureSnackBar
					}
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
