package com.braisgabin.interview.newstech.home.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.braisgabin.interview.newstech.MockHomePresenter
import com.braisgabin.interview.newstech.R
import com.braisgabin.interview.newstech.entity.Photo
import com.braisgabin.interview.newstech.home.presentation.feature.State
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

  @Rule
  @JvmField
  var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

  private val presenter: MockHomePresenter
    get() = activityRule.activity.presenter as MockHomePresenter

  fun runOnUiThread(task: () -> Unit) {
    activityRule.runOnUiThread(task)
  }

  @After
  fun tearDown() {
    verifyNoMoreInteractions(presenter.events)
  }

  @Test
  fun showLoading() {

    onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
    onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
  }

  @Test
  fun showRetry() {
    runOnUiThread { presenter.emit(State.Error) }

    onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    onView(withId(R.id.retry)).check(matches(isDisplayed()))
    onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
  }

  @Test
  fun clickRetry() {
    runOnUiThread { presenter.emit(State.Error) }

    onView(withId(R.id.retry)).perform(click())

    verify(presenter.events).accept(HomeIntent.Retry)
  }

  @Test
  fun showData() {
    runOnUiThread { presenter.emit(State.Data(emptyList(), loading = false, refreshing = false)) }

    onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
  }

  @Test
  fun refresh() {
    runOnUiThread { presenter.emit(State.Data(emptyList(), loading = false, refreshing = false)) }

    onView(withId(R.id.recyclerView)).perform(swipeDown())

    verify(presenter.events).accept(HomeIntent.Refresh)
  }

  @Test
  fun clickPhoto() {
    val photo = Photo("1", "url", "thumbnail")
    runOnUiThread {
      presenter.emit(
        State.Data(listOf(photo), loading = false, refreshing = false)
      )
    }

    onView(withId(R.id.recyclerView)).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

    verify(presenter.events).accept(HomeIntent.PhotoSelected(photo))
  }
}
