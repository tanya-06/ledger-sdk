package lib.dehaat.ledger.util

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.ledger.downloadledger.annotations.DownloadLedgerFormat
import javax.inject.Inject
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.presentation.model.invoicedownload.ProgressData


class DownloadFileUtil @Inject constructor(
    @ApplicationContext val context: Context
) {

    suspend fun downloadPDF(
        url: String,
        fileName: String,
        invoiceDownloadStatus: (InvoiceDownloadData) -> Unit,
        updateDownloadStatus: (String, DownloadStatus, (InvoiceDownloadData) -> Unit) -> Unit
    ) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
            .setMimeType(LedgerConstants.PDF_MIME_TYPE)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS.toString(),
                "/DeHaat/$fileName.pdf"
            )
        val downloadId = downloadManager?.enqueue(request)
        handleDownloadProgress(downloadId, fileName, invoiceDownloadStatus, updateDownloadStatus)
    }

    fun downloadFile(
        url: String,
        fName: String?,
        format: String
    ) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        var fileName = fName ?: url.substring(url.lastIndexOf('/') + 1)
        if (fileName.startsWith("/") && fileName.length > 1) {
			fileName = fileName.substring(1)
		}
        fileName = fileName.substring(0, 1).uppercase() + fileName.substring(1)
        val request = DownloadManager.Request(Uri.parse(url))
            .setMimeType(getMimeType(format))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDescription(context.getString(R.string.ledger_download))
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS.toString(),
                "/$fileName${getFileExtension(format)}"
            )
        downloadManager?.enqueue(request)
        if (format == DownloadLedgerFormat.EXCEL) {
            checkForAppAvailability()
        }
    }

    private fun getFileExtension(format: String) = when (format) {
        DownloadLedgerFormat.PDF -> ".pdf"
        else -> ".xlsx"
    }

    private fun getMimeType(format: String) = when (format) {
        DownloadLedgerFormat.PDF -> LedgerConstants.PDF_MIME_TYPE
        else -> LedgerConstants.XLSX_MIME_TYPE
    }

    private fun checkForAppAvailability() {
        val mimeTypeMap = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            "xlsx"
        )
        context.packageManager.queryIntentActivities(
            Intent(Intent.ACTION_VIEW).apply {
                type = mimeTypeMap
            },
            PackageManager.MATCH_DEFAULT_ONLY
        ).also {
            if (it.isEmpty()) {
                Toast.makeText(context, "Please Install XLSX reader", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun handleDownloadProgress(
        downloadId: Long?,
        fileName: String,
        invoiceDownloadStatus: (InvoiceDownloadData) -> Unit,
        updateDownloadStatus: (String, DownloadStatus, (InvoiceDownloadData) -> Unit) -> Unit
    ) {
        if (downloadId == null)
            return

        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager

        var finishDownload = false
        val downloadStatus = DownloadStatus()
        try {
            while (!finishDownload) {
                val cursor = downloadManager?.query(
                    DownloadManager.Query().setFilterById(
                        downloadId
                    )
                )

                if (cursor?.moveToFirst() == true) {
                    val columnIndex =
                        cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI)
                    val localUriString = cursor.getStringOrNull(columnIndex)
                    val index = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                    if (index >= 0) {
                        when (cursor.getIntOrNull(index)) {
                            DownloadManager.STATUS_FAILED -> {
                                downloadStatus.status = DownloadManager.STATUS_FAILED
                                finishDownload = true
                                localUriString?.let { uriString ->
                                    Uri.parse(uriString).path?.let {
                                        downloadStatus.filePath = it
                                    }
                                }
                                updateDownloadStatus(
                                    fileName,
                                    downloadStatus,
                                    invoiceDownloadStatus
                                )
                            }

                            DownloadManager.STATUS_RUNNING -> {
                                downloadStatus.status = DownloadManager.STATUS_RUNNING
                                val totalSizeIndex =
                                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                                if (totalSizeIndex >= 0) {
                                    val totalBytes = cursor.getLong(totalSizeIndex) ?: 0L
                                    if (totalBytes >= 0) {
                                        val downloadedIndex =
                                            cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                                        val downloadedBytes =
                                            cursor.getLongOrNull(downloadedIndex) ?: 0L
                                        downloadStatus.progressData =
                                            ProgressData(
                                                downloadedBytes.toInt(),
                                                totalBytes.toInt()
                                            )
                                        updateDownloadStatus(
                                            fileName,
                                            downloadStatus,
                                            invoiceDownloadStatus
                                        )
                                    }
                                }
                            }

                            DownloadManager.STATUS_SUCCESSFUL -> {
                                downloadStatus.status = DownloadManager.STATUS_SUCCESSFUL
                                finishDownload = true
                                localUriString?.let { uriString ->
                                    Uri.parse(uriString).path?.let {
                                        downloadStatus.filePath = it
                                    }
                                }
                                updateDownloadStatus(
                                    fileName,
                                    downloadStatus,
                                    invoiceDownloadStatus
                                )
                            }
                        }
                    }
                }
            }
            delay(500)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
