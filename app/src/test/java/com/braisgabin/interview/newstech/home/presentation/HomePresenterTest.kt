package com.braisgabin.interview.newstech.home.presentation

import com.braisgabin.interview.newstech.home.presentation.feature.HomeFeature
import com.braisgabin.interview.newstech.home.presentation.feature.State
import com.braisgabin.interview.newstech.home.presentation.feature.Wish
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import io.reactivex.Observer
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomePresenterTest {
  private lateinit var stateObserver: Observer<State>
  private lateinit var testSubscriber: TestSubscriber<State>

  private val feature: HomeFeature = mock()
  private val presenter = HomePresenter(feature)

  @Before
  fun setUp() {
    testSubscriber = presenter.states
      .test()
    argumentCaptor<Observer<State>>().apply {
      verify(feature).subscribe(capture())
      stateObserver = firstValue
    }
  }

  @After
  fun tearDown() {
    verifyNoMoreInteractions(feature)
  }

  @Test
  fun sendRetryIntent() {
    presenter.events.accept(HomeIntent.Retry)

    verify(feature).accept(Wish.Retry)
  }

  @Test
  fun sendRefreshIntent() {
    presenter.events.accept(HomeIntent.Refresh)

    verify(feature).accept(Wish.Refresh)
  }

  @Test
  fun emitNewState() {
    stateObserver.onNext(State.Loading)

    testSubscriber.assertValue(State.Loading)
  }
}
