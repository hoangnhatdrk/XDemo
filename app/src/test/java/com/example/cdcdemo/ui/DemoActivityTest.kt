package com.example.cdcdemo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cdcdemo.R
import com.example.cdcdemo.data.CurrencyInfo
import com.example.cdcdemo.sampleCurrencyInfo
import com.example.cdcdemo.ui.currency.CurrencyViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@ExperimentalCoroutinesApi
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class DemoActivityTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val fakeViewModelItemClickFlow = MutableSharedFlow<CurrencyInfo>()
    @BindValue
    val viewModel= mockk<CurrencyViewModel>(relaxed = true).apply {
        every { itemClick } returns fakeViewModelItemClickFlow.asSharedFlow()
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
    fun `when init, no fragment shown and load btn enabled and sort btn disabled`() {
        val scenario = launch(DemoActivity::class.java)

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onActivity { activity ->
            assert(activity.supportFragmentManager.findFragmentById(R.id.fragment_container) == null)
        }

        onView(withId(R.id.btn1))
            .check { view, _ -> view.isEnabled }
        onView(withId(R.id.btn2))
            .check { view, _ -> !view.isEnabled }
    }

    @Test
    fun `when click load btn, show fragment, load btn disabled, sort btn enabled`() {
        val scenario = launch(DemoActivity::class.java)

        onView(withId(R.id.btn1))
            .perform(click())

        scenario.onActivity { activity ->
            assert(activity.supportFragmentManager.findFragmentById(R.id.fragment_container) != null)
        }

        onView(withId(R.id.btn1))
            .check { view, _ -> !view.isEnabled }
        onView(withId(R.id.btn2))
            .check { view, _ -> view.isEnabled }
    }

    @Test
    fun `when click sort btn, trigger viewmodel toggleSort()`() {
        launch(DemoActivity::class.java)

        onView(withId(R.id.btn1))
            .perform(click())

        onView(withId(R.id.btn2))
            .perform(click())

        verify { viewModel.toggleSort() }
    }

    @Test
    fun `when viewmodel emit item click, show toast message`() = runBlockingTest {
        launch(DemoActivity::class.java)

        val sample = sampleCurrencyInfo(1)
        fakeViewModelItemClickFlow.emit(sample)
        assert(ShadowToast.showedToast("Pumping ${sample.symbol} ..."))
    }
}