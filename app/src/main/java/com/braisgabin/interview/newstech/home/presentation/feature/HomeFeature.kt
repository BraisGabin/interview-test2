package com.braisgabin.interview.newstech.home.presentation.feature

import com.badoo.mvicore.feature.BaseFeature
import com.braisgabin.interview.newstech.entity.Photo
import com.braisgabin.interview.newstech.utils.Mockable
import io.reactivex.Observable
import javax.inject.Inject

@Mockable
class HomeFeature @Inject constructor(
  actor: HomeActor,
  reducer: HomeReducer
) : BaseFeature<Wish, Action, Effect, State, Nothing>(
  initialState = State.Loading,
  actor = actor,
  reducer = reducer,
  bootstrapper = { Observable.just(Action.Reload) },
  wishToAction = { wish ->
    when (wish) {
      Wish.Retry,
      Wish.Refresh -> Action.Reload
    }
  }
)

sealed class Wish {
  object Retry : Wish()
  object Refresh : Wish()
}

sealed class Action {
  object Reload : Action()
}

sealed class State {
  object Loading : State()
  object Error : State()
  data class Data(
    val photos: List<Photo>,
    val loading: Boolean,
    val refreshing: Boolean
  ) : State()
}

sealed class Effect {
  object Error : Effect()
  object Loading : Effect()
  object NoMoreItems : Effect()
  data class Data(val photos: List<Photo>) : Effect()
}
