package lib.dehaat.ledger.di

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
import retrofit2.Retrofit

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
