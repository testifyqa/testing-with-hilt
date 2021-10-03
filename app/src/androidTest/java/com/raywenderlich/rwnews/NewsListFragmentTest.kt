package com.raywenderlich.rwnews

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.raywenderlich.rwnews.di.AppModule
import com.raywenderlich.rwnews.logger.RwNewsLogger
import com.raywenderlich.rwnews.repository.NewsRepository
import com.raywenderlich.rwnews.repository.entity.News
import com.raywenderlich.rwnews.ui.list.NewsListFragment
import com.raywenderlich.rwnews.ui.list.NewsListItemViewHolder
import com.raywenderlich.rwnews.util.launchFragmentInHiltContainer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import fakes.FakeNewsLogger
import fakes.FakeNewsRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NewsListFragmentTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltAndroidRule.inject()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object TestAppModule {
        @Provides
        fun provideNewsRepository(): NewsRepository =
            FakeNewsRepository().apply {
                insert(News(1, "First Title", "First Body"))
                insert(News(2, "Second Title", "Second Body"))
                insert(News(3, "Third Title", "Third Body"))
            }

        @Provides
        fun provideNewsLogger(): RwNewsLogger = FakeNewsLogger()
    }

    @Test
    fun whenDisplayed_newsListFromRepoIsDisplayed() {
        launchFragmentInHiltContainer<NewsListFragment>()
        scrollAtAndCheckTestVisible(0, "First Title")
        scrollAtAndCheckTestVisible(1, "Second Title")
        scrollAtAndCheckTestVisible(2, "Third Title")
    }

    private fun scrollAtAndCheckTestVisible(position: Int, text: String) {
        onView(ViewMatchers.withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions
                .scrollToPosition<NewsListItemViewHolder>(position))
        onView(withText(text)).check(matches(isDisplayed()))
    }
}