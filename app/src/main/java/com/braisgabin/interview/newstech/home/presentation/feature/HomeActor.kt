package com.braisgabin.interview.newstech.home.presentation.feature

import com.badoo.mvicore.element.Actor
import com.braisgabin.interview.newstech.home.domain.PhotosUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

class HomeActor(
  private val photoUseCase: PhotosUseCase,
  private val main: Scheduler
) : Actor<State, Action, Effect> {

  override fun invoke(state: State, action: Action): Observable<out Effect> {
    return when (action) {
      Action.Reload -> photoUseCase.photos()
        .sample(33, TimeUnit.MILLISECONDS, main, true)
        .map<Effect> { either ->
          either.fold(
            { Effect.Error },
            { Effect.Data(it) })
        }
        .observeOn(main)
        .defaultIfEmpty(Effect.Data(emptyList()))
        .startWith(Effect.Loading)
        .concatWith(Flowable.just(Effect.NoMoreItems))
    }
      .toObservable()
  }
}
