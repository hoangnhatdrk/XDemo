package com.example.cdcdemo.data

import android.content.res.AssetManager
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cdcdemo.data.sample.SAMPLE_CURRENCY_LIST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [CurrencyInfo::class], version = 1)
abstract class CurrencyDatabase: RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao

    class Callback @Inject constructor(
        private val dbProvider: Provider<CurrencyDatabase>,
        private val coroutineScope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = dbProvider.get().getCurrencyDao()

            coroutineScope.launch(Dispatchers.IO) {
                dao.insertAll(
                    SAMPLE_CURRENCY_LIST
                )
            }
        }
    }
}