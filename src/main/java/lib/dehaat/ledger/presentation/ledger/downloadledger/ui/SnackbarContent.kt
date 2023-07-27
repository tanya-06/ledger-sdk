package lib.dehaat.ledger.presentation.ledger.downloadledger.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import lib.dehaat.ledger.R
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.model.downloadledger.DownloadStatusData
import lib.dehaat.ledger.presentation.model.downloadledger.SnackBarType
import lib.dehaat.ledger.resources.Color2969E
import lib.dehaat.ledger.resources.Color80D12E
import lib.dehaat.ledger.resources.Neutral30
import lib.dehaat.ledger.resources.text12Sp
import lib.dehaat.ledger.resources.textMedium16Sp
import lib.dehaat.ledger.util.FileUtils.openFile


@Composable
fun SnackbarHostContent(
	snackbarType: SnackBarType?, snackbarData: SnackbarData,
	scaffoldState: ScaffoldState, downloadLedger: () -> Unit
) = when (snackbarType) {
	is SnackBarType.DownloadSuccess -> DownloadSuccessSnackbar(
		snackbarType, scaffoldState.snackbarHostState
	)

	is SnackBarType.DownloadFailed -> DownloadFailed(
		scaffoldState.snackbarHostState, downloadLedger
	)

	is SnackBarType.DownloadProgress -> DownloadInProgress(scaffoldState.snackbarHostState)
	else -> Text(snackbarData.message)
}

@Composable
private fun DownloadInProgress(snackbarHostState: SnackbarHostState) =
	Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
		Image(
			painter = painterResource(R.drawable.ic_downloading),
			contentDescription = null,
			modifier = Modifier.size(18.dp)
		)
		Column(
			Modifier
				.weight(1f)
				.padding(start = 4.dp, end = 12.dp)
		) {
			Text(
				text = stringResource(R.string.download_in_progress),
				style = textMedium16Sp(Color.White)
			)
			Text(
				text = stringResource(R.string.statement_download_soon),
				style = text12Sp(Neutral30)
			)
		}
		Image(
			painter = painterResource(R.drawable.ledger_ic_cancel),
			contentDescription = null,
			colorFilter = ColorFilter.tint(Color.White),
			modifier = Modifier
				.size(20.dp)
				.padding(2.dp)
				.clickable { snackbarHostState.currentSnackbarData?.dismiss() }
		)
	}

@Composable
private fun DownloadFailed(snackbarHostState: SnackbarHostState, downloadLedger: () -> Unit) =
	Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
		Image(
			painter = painterResource(R.drawable.ic_download_failed),
			contentDescription = null,
			modifier = Modifier.size(18.dp)
		)
		Text(
			text = stringResource(R.string.download_failed),
			style = textMedium16Sp(Color2969E),
			modifier = Modifier.padding(start = 4.dp, end = 12.dp)
		)
		Text(
			text = stringResource(R.string.try_again),
			style = text12Sp(Color.White),
			textDecoration = TextDecoration.Underline,
			modifier = Modifier
				.clickable {
					snackbarHostState.currentSnackbarData?.dismiss()
					downloadLedger()
				}
		)
		Spacer(modifier = Modifier.weight(1f))
		Image(
			painter = painterResource(R.drawable.ledger_ic_cancel),
			contentDescription = null,
			colorFilter = ColorFilter.tint(Color.White),
			modifier = Modifier
				.size(20.dp)
				.padding(2.dp)
				.clickable { snackbarHostState.currentSnackbarData?.dismiss() }
		)
	}

@Composable
private fun DownloadSuccessSnackbar(
	snackbarType: SnackBarType.DownloadSuccess, snackbarHostState: SnackbarHostState
) = Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
	val context = LocalContext.current
	Image(
		painter = painterResource(R.drawable.ic_download_success),
		contentDescription = null,
		modifier = Modifier.size(16.dp)
	)
	Text(
		text = stringResource(R.string.download_completed),
		style = textMedium16Sp(Color80D12E),
		modifier = Modifier.padding(start = 4.dp, end = 12.dp)
	)
	Text(
		text = stringResource(R.string.view_statement),
		style = text12Sp(Color.White),
		textDecoration = TextDecoration.Underline,
		modifier = Modifier
			.clickable { openFile(context, snackbarType.filePath, snackbarType.mimeType) }
	)
	Spacer(modifier = Modifier.weight(1f))
	Image(
		painter = painterResource(R.drawable.ledger_ic_cancel),
		contentDescription = null,
		colorFilter = ColorFilter.tint(Color.White),
		modifier = Modifier
			.size(20.dp)
			.padding(2.dp)
			.clickable { snackbarHostState.currentSnackbarData?.dismiss() }
	)
}


fun registerDownloadReceiver(context: Context) {
	context.registerReceiver(
		downloadBroadCastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
	)
}

fun unRegisterDownloadReceiver(context: Context) {
	context.unregisterReceiver(downloadBroadCastReceiver)
}

private val downloadBroadCastReceiver = object : BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent?) {
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent?.action) {
			val query = DownloadManager.Query()
			query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
			val manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
			val cursor = manager.query(query)
			if (cursor.moveToFirst()) {
				if (cursor.count > 0) {
					val downloadLocalUri =
						cursor.getStringOrNull(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
					val downloadMimeType =
						cursor.getStringOrNull(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE))
					when (cursor.getIntOrNull(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
						DownloadManager.STATUS_SUCCESSFUL -> LedgerSDK.downloadStatusLiveData.postSingleEventValue(
							DownloadStatusData(
								true, Uri.parse(downloadLocalUri).run {
									if (ContentResolver.SCHEME_FILE == scheme) path else downloadLocalUri
								}, downloadMimeType
							)
						)

						DownloadManager.STATUS_FAILED -> LedgerSDK.downloadStatusLiveData.postSingleEventValue(
							DownloadStatusData(false)
						)
					}
				}
			}
			cursor.close()
		}
	}
}
