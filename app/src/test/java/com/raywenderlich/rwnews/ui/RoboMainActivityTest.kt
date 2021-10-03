package com.raywenderlich.rwnews.ui

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.raywenderlich.rwnews.R
import com.raywenderlich.rwnews.di.ActivityModule
import com.raywenderlich.rwnews.ui.list.NewsListFragment
import com.raywenderlich.rwnews.ui.navigation.NavigationHelper
import dagger.hilt.android.testing.*
import fakes.FakeNavigationHelper

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@UninstallModules(ActivityModule::class)
class RoboMainActivityTest {

    @BindValue
    @JvmField
    val navigator: NavigationHelper = FakeNavigationHelper()

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun whenMainActivityLaunchedNavigationHelperIsInvokedForFragment() {
        activityScenarioRule.scenario // 1. launch activity defined in ActivityScenario
        val fakeHelper = navigator as FakeNavigationHelper // 2. Access navigator after casting to FakeNavigationHelper
        with(fakeHelper.replaceRequests[0]) { // 3
            assertThat(anchorId)
                .isEqualTo(R.id.anchor)
            assertThat(fragment)
                .isInstanceOf(NewsListFragment::class.java)
            assertThat(backStack)
                .isNull()
        }
    }
}

