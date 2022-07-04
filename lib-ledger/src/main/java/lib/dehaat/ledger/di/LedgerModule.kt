package lib.dehaat.ledger.di

import com.dehaat.androidbase.coroutine.Dispatchers
import com.dehaat.androidbase.coroutine.IDispatchers
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lib.dehaat.ledger.data.LedgerRepository
import lib.dehaat.ledger.data.source.ILedgerDataSource
import lib.dehaat.ledger.domain.ILedgerRepository
import lib.dehaat.ledger.framework.network.LedgerAPIService
import lib.dehaat.ledger.framework.network.LedgerDataSource
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LedgerModule {

    @Binds
    abstract fun provideLedgerDataSource(
        remote: LedgerDataSource
    ): ILedgerDataSource

    @Binds
    abstract fun provideLedgerRepository(impl: LedgerRepository): ILedgerRepository

    companion object {

        @Provides
        fun provideLedgerAPIService(
            retrofit: Retrofit
        ): LedgerAPIService = retrofit.create(LedgerAPIService::class.java)
    }
}
