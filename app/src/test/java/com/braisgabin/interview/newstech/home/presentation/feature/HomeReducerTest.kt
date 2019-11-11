package com.braisgabin.interview.newstech.home.presentation.feature

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class HomeReducerTest {
  private val reducer = HomeReducer()

  @Test
  fun error() {
    assertThat<State>(
      reducer.invoke(State.Loading, Effect.Error),
      `is`(State.Error as State)
    )
  }

  @Test
  fun loading_noData() {
    assertThat<State>(
      reducer.invoke(State.Error, Effect.Loading),
      `is`(State.Loading as State)
    )
  }

  @Test
  fun loading_data() {
    assertThat<State>(
      reducer.invoke(State.Data(emptyList(), false, false), Effect.Loading),
      `is`(State.Data(emptyList(), false, true) as State)
    )
  }

  @Test
  fun noMoreItems_noData() {
    assertThat<State>(
      reducer.invoke(State.Error, Effect.NoMoreItems),
      `is`(State.Error as State)
    )
  }

  @Test
  fun noMoreItems_data() {
    assertThat<State>(
      reducer.invoke(State.Data(emptyList(), true, false), Effect.NoMoreItems),
      `is`(State.Data(emptyList(), false, false) as State)
    )
  }

  @Test
  fun data() {
    assertThat<State>(
      reducer.invoke(State.Loading, Effect.Data(emptyList())),
      `is`(State.Data(emptyList(), true, false) as State)
    )
  }
}
