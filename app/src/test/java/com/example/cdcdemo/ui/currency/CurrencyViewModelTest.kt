package com.example.cdcdemo.ui.currency

import app.cash.turbine.test
import com.example.cdcdemo.data.CurrencyDao
import com.example.cdcdemo.data.CurrencyInfo
import com.example.cdcdemo.data.sample.SAMPLE_CURRENCY_LIST
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testDao = mockk<CurrencyDao>()
    private lateinit var testSubject: CurrencyViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { testDao.getAll() } returns flow { emit(SAMPLE_CURRENCY_LIST) }
        testSubject = CurrencyViewModel(testDao, testDispatcher, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `When init viewmodel should trigger Dao getAll() and update currencyList with same value`() = runBlocking {
        verify { testDao.getAll() }
        testSubject.currencyList.test {
            assert(awaitItem() == SAMPLE_CURRENCY_LIST)
        }
    }

    @Test
    fun `When call toggleSort should first sort data in name ascending order`() = runBlocking {
        testSubject.currencyList.test {
            awaitItem()
            testSubject.toggleSort()
            assert(awaitItem() == SAMPLE_CURRENCY_LIST.sortedBy { it.name })
        }
    }

    @Test
    fun `When call toggleSort more than once should toggle between name ascending and name descending order`() = runBlocking {
        testSubject.currencyList.test {
            awaitItem()
            testSubject.toggleSort()
            assert(awaitItem() == SAMPLE_CURRENCY_LIST.sortedBy { it.name })
            testSubject.toggleSort()
            assert(awaitItem() == SAMPLE_CURRENCY_LIST.sortedByDescending { it.name })
            testSubject.toggleSort()
            assert(awaitItem() == SAMPLE_CURRENCY_LIST.sortedBy { it.name })
        }
    }

    @Test
    fun `When call toggleSort while sorting should ignore`() = runBlocking {
        testSubject.currencyList.test {
            awaitItem()
            testDispatcher.pauseDispatcher()
            testSubject.toggleSort()
            testSubject.toggleSort()
            testDispatcher.resumeDispatcher()
            assert(awaitItem() == SAMPLE_CURRENCY_LIST.sortedBy { it.name })
        }
    }
}