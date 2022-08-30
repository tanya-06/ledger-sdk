package lib.dehaat.ledger.initializer

import android.content.Context
import androidx.annotation.DrawableRes
import com.facebook.drawee.backends.pipeline.Fresco
import lib.dehaat.ledger.presentation.ledger.LedgerDetailActivity

object LedgerSDK {
    lateinit var currentApp: LedgerParentApp
    lateinit var bucket: String
    var appIcon: Int = 0
        private set

    fun init(
        context: Context,
        app: LedgerParentApp,
        bucket: String,
        @DrawableRes appIcon: Int,
        debugMode: Boolean
    ) {
        currentApp = app
        this.bucket = bucket
        this.appIcon = appIcon
        this.isDebug = debugMode
        Fresco.initialize(context)
    }

    fun isCurrentAppAvailable() = ::currentApp.isInitialized && ::bucket.isInitialized

    fun openLedger(
        context: Context,
        partnerId: String,
        dcName: String,
        language: String? = null
    ) = if (isCurrentAppAvailable()) {
        LedgerDetailActivity.Companion.Args(
            partnerId = partnerId,
            dcName = dcName,
            language = language
        ).also {
            context.startActivity(it.build(context))
        }
    } else {
        throw Exception("Ledger not initialised Exception")
    }

    val isDBA: Boolean
        get() = currentApp is LedgerParentApp.DBA

    val isAIMS: Boolean
        get() = currentApp is LedgerParentApp.AIMS

    var isDebug: Boolean = false
        private set
}
