package lib.dehaat.ledger.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.core.content.FileProvider
import com.dehaat.androidbase.helper.showToast
import lib.dehaat.ledger.R
import lib.dehaat.ledger.presentation.LedgerConstants
import java.io.File
import java.io.IOException

object FileUtils {

	private const val FILE_TYPE_PDF = "pdf"

	fun getFileFromBase64(
		base64: String,
		fileType: String?,
		dir: File?,
		fileName: String,
	) = if (fileType == FILE_TYPE_PDF) {
		getPdfFromBase64(base64, dir, fileName)
	} else null

	private fun getPdfFromBase64(
		base64Pdf: String,
		dir: File?,
		fileName: String,
	) = try {
		val pdfAsBytes: ByteArray = Base64.decode(base64Pdf, Base64.DEFAULT)
		val pdfFile = File(dir, "$fileName.pdf")
		if (!pdfFile.exists()) {
			pdfFile.parentFile?.mkdirs()
			pdfFile.createNewFile()
		}
		pdfFile.writeBytes(pdfAsBytes)
		pdfFile
	} catch (e: IOException) {
		e.printStackTrace()
		null
	}

	fun openFile(context: Context, filePath: String, mimeType: String?) {
		val file = File(filePath)
		if (file.exists()) {
			Intent(Intent.ACTION_VIEW).apply {
				val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					FileProvider.getUriForFile(context, context.packageName + ".provider", file)
				} else {
					Uri.fromFile(file)
				}
				setDataAndType(uri, mimeType ?: LedgerConstants.PDF_MIME_TYPE)
				addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
				try {
					context.startActivity(this@apply)
				} catch (e: Exception) {
					context.showToast(context.getString(R.string.app_not_found))
				}
			}
		} else {
			context.showToast(context.getString(R.string.file_not_found))
		}
	}
}
