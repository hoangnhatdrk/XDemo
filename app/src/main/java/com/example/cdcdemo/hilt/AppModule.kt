package com.example.cdcdemo.hilt

import android.app.Application
import androidx.room.Room
import com.example.cdcdemo.data.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        dbCallback: CurrencyDatabase.Callback
    ) = Room.databaseBuilder(app, CurrencyDatabase::class.java, "currency_database")
        .fallbackToDestructiveMigration()
        .addCallback(dbCallback)
        .build()

    @Provides
    fun provideDao(db: CurrencyDatabase) = db.getCurrencyDao()

    @Provides
    @Singleton
    fun provideAppScope() = CoroutineScope(SupervisorJob())

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}

@Retention
@Qualifier
annotation class IODispatcher

@Retention
@Qualifier
annotation class DefaultDispatcher