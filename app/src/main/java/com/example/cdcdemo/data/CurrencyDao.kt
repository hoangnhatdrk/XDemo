package com.example.cdcdemo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(currencyInfo: List<CurrencyInfo>)

    @Query("SELECT * FROM currency_table")
    fun getAll(): Flow<List<CurrencyInfo>>
}