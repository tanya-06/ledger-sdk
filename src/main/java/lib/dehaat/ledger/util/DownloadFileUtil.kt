package lib.dehaat.ledger.util

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.amazonaws.ClientConfiguration
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.LedgerConstants


class DownloadFileUtil @Inject constructor(
    @ApplicationContext val context: Context
) {

    private fun File.createFileIfNotExists() =
        try {
            if (!exists()) {
                createNewFile()
            } else {
                deleteRecursively()
                createNewFile()
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
            false
        }

    private fun createTransferUtilityBuilder() = TransferUtility
        .builder()
        .s3Client(getAWSClient())
        .context(context)
        .build()

    private fun getAWSClient(): AmazonS3Client {
        val s3Client = AmazonS3Client(
            AWSMobileClient.getInstance().credentialsProvider,
            ClientConfiguration()
        )
        if (LedgerSDK.isDebug) {
            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTH_1))
        } else {
            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1))
        }
        return s3Client
    }

    fun downloadFile(
        file: File,
        fileNameKey: String,
        bucket: String
    ) = try {
        if (file.createFileIfNotExists())
            downloadWithTransferUtility(fileNameKey, file, bucket)
        else
            null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun downloadWithTransferUtility(
        fileName: String,
        directory: File,
        bucket: String
    ): TransferObserver {
        val transferUtility = createTransferUtilityBuilder()
        return transferUtility.download(bucket, fileName, directory)
    }

    fun downloadPDF(
        url: String,
        fName: String?
    ) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        var fileName = fName ?: url.substring(url.lastIndexOf('/') + 1)
        fileName = fileName.substring(0, 1).uppercase() + fileName.substring(1)
        val request = DownloadManager.Request(Uri.parse(url))
            .setMimeType(LedgerConstants.PDF_MIME_TYPE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDescription(context.getString(R.string.ledger_download))
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS.toString(),
                "/$fileName.pdf"
            )
        downloadManager?.enqueue(request)
    }

    fun downloadExcel(
        url: String,
        fName: String?
    ) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        var fileName = fName ?: url.substring(url.lastIndexOf('/') + 1)
        fileName = fileName.substring(0, 1).uppercase() + fileName.substring(1)
        val request = DownloadManager.Request(Uri.parse(url))
            .setMimeType(LedgerConstants.XLSX_MIME_TYPE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDescription(context.getString(R.string.ledger_download))
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS.toString(),
                "/$fileName.xlsx"
            )
        downloadManager?.enqueue(request)
        checkForAppAvailability()
    }

    private fun checkForAppAvailability() {
        val mimeTypeMap = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            "xlsx"
        )
        val test = Intent(Intent.ACTION_VIEW)
        test.type = mimeTypeMap
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
}
