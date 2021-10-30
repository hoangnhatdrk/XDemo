package com.example.cdcdemo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert
    suspend fun insertAll(currencyInfo: List<CurrencyInfo>)

    @Query("SELECT * FROM currency_table")
    fun getAll(): Flow<List<CurrencyInfo>>
}