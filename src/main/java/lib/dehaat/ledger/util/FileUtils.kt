package lib.dehaat.ledger.util

import android.util.Base64
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
}
