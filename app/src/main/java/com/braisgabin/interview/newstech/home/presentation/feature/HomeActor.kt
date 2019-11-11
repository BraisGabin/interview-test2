package com.braisgabin.interview.newstech.home.presentation.feature

import com.badoo.mvicore.element.Actor
import com.braisgabin.interview.newstech.home.domain.PhotosUseCase
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class HomeActor @Inject constructor(
  private val photoUseCase: PhotosUseCase,
  @Named("main") private val main: Scheduler
) : Actor<State, Action, Effect> {

  override fun invoke(state: State, action: Action): Observable<out Effect> {
    return when (action) {
      Action.Reload -> photoUseCase.photos()
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
