package com.example.cdcdemo.ui.currency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cdcdemo.R
import com.example.cdcdemo.data.CurrencyInfo
import com.example.cdcdemo.launchFragmentInHiltContainer
import com.example.cdcdemo.sampleCurrencyInfo
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class CurrencyListFragmentTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val fakeViewModelItemListFlow = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    @BindValue
    val viewModel= mockk<CurrencyViewModel>(relaxed = true).apply {
        every { currencyList } returns fakeViewModelItemListFlow.asStateFlow()
    }

    @Before
    fun init() {
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when init, should show recyclerview with empty`() {
        launchFragmentInHiltContainer<CurrencyListFragment> {
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun `when viewmodel emit non-empty list currency, should populate recyclerview`() {
        launchFragmentInHiltContainer<CurrencyListFragment> {
            runBlockingTest {
                val sampleList = listOf(
                    sampleCurrencyInfo(1),
                    sampleCurrencyInfo(2),
                    sampleCurrencyInfo(3)
                )
                fakeViewModelItemListFlow.emit(sampleList)

                sampleList.forEach {
                    onView(withId(R.id.recyclerView))
                        .perform(
                            RecyclerViewActions.scrollTo<CurrencyAdapter.CurrencyViewHodler>(
                                hasDescendant(withText(it.name))
                            )
                        )
                }
            }
        }
    }
}