package com.braisgabin.interview.newstech.home.presentation.feature

import com.badoo.mvicore.element.Reducer
import javax.inject.Inject

class HomeReducer @Inject constructor() : Reducer<State, Effect> {
  override fun invoke(state: State, effect: Effect): State {
    return when (effect) {
      Effect.Error -> State.Error
      Effect.Loading -> if (state is State.Data) {
        state.copy(refreshing = true)
      } else {
        State.Loading
      }
      Effect.NoMoreItems -> if (state is State.Data) {
        state.copy(loading = false)
      } else {
        state
      }
      is Effect.Data -> State.Data(effect.photos, loading = true, refreshing = false)
    }
  }
}
