package com.example.cdcdemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_table")
data class CurrencyInfo(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "symbol")val symbol: String
)
