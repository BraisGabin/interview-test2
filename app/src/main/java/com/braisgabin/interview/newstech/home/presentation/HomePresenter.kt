package com.braisgabin.interview.newstech.home.presentation

import androidx.lifecycle.ViewModel
import com.braisgabin.interview.newstech.Navigator
import com.braisgabin.interview.newstech.entity.Photo
import com.braisgabin.interview.newstech.home.presentation.feature.HomeFeature
import com.braisgabin.interview.newstech.home.presentation.feature.State
import com.braisgabin.interview.newstech.home.presentation.feature.Wish
import com.braisgabin.interview.newstech.utils.Presenter
import com.braisgabin.interview.newstech.utils.exhaustive
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class HomePresenter @Inject constructor(
  feature: HomeFeature,
  navigator: Navigator
) : ViewModel(), Presenter<HomeIntent, State> {
  private val disposable = CompositeDisposable()

  override val events: Consumer<HomeIntent>
  override val states: Flowable<State> = Observable.wrap(feature)
    .replay(1)
    .autoConnect(1) { disposable.add(it) }
    .toFlowable(BackpressureStrategy.LATEST)

  init {
    disposable.add(feature)

    val relay: Relay<HomeIntent> = PublishRelay.create()
    disposable.add(relay
      .subscribe { intent ->
        when (intent) {
          HomeIntent.Retry -> feature.accept(Wish.Retry)
          HomeIntent.Refresh -> feature.accept(Wish.Refresh)
          is HomeIntent.PhotoSelected -> navigator.goToDetail(intent.photo.url)
        }.exhaustive
      })
    events = relay
  }

  override fun onCleared() {
    disposable.dispose()
  }
}

sealed class HomeIntent {
  object Retry : HomeIntent()
  object Refresh : HomeIntent()
  data class PhotoSelected(val photo: Photo) : HomeIntent()
}
