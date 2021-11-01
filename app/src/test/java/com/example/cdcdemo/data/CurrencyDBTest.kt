package com.example.cdcdemo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.example.cdcdemo.sampleCurrencyInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CurrencyDBTest {
    private lateinit var database: CurrencyDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CurrencyDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun getAll_should_return_all_latest_data() = runBlockingTest {
        database.getCurrencyDao().getAll().test {
            assert(awaitItem().isEmpty())
            database.getCurrencyDao().insertAll(listOf(
                sampleCurrencyInfo(1),
                sampleCurrencyInfo(2),
                sampleCurrencyInfo(3)
            ))
            val newList = awaitItem()
            assert(newList.size == 3)
            assert(newList[0] == sampleCurrencyInfo(1))
            assert(newList[1] == sampleCurrencyInfo(2))
            assert(newList[2] == sampleCurrencyInfo(3))
        }
    }

    @Test
    fun insert_should_ignore_existing_if_conflict() = runBlockingTest {
        database.getCurrencyDao().getAll().test {
            assert(awaitItem().isEmpty())
            database.getCurrencyDao().insertAll(listOf(
                sampleCurrencyInfo(1, "Custom name")
            ))
            var newList = awaitItem()
            assert(newList.size == 1)
            database.getCurrencyDao().insertAll(listOf(
                sampleCurrencyInfo(1),
                sampleCurrencyInfo(2),
                sampleCurrencyInfo(3)
            ))
            newList = awaitItem()
            assert(newList.size == 3)
            assert(newList[0].name == "Custom name")
            assert(newList[1] == sampleCurrencyInfo(2))
            assert(newList[2] == sampleCurrencyInfo(3))
        }
    }
}