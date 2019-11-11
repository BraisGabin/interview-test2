package com.braisgabin.interview.newstech

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.braisgabin.interview.newstech.home.presentation.HomeIntent
import com.braisgabin.interview.newstech.home.presentation.MainActivity
import com.braisgabin.interview.newstech.home.presentation.feature.State
import com.braisgabin.interview.newstech.utils.Presenter
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Consumer

class MockApp : Application(), BaseApp {

  // This could by done creating a MockAppComponent and using an instance of it
  // But for this easy case with this is enough.
  override val component: AppComponent
    get() = object : AppComponent {
      override fun mainActivityComponent(): MainActivity.Component.Factory {
        return object : MainActivity.Component.Factory {
          override fun create(activity: AppCompatActivity): MainActivity.Component {
            return object : MainActivity.Component {
              override fun inject(activity: MainActivity) {
                activity.presenter = MockHomePresenter()
              }
            }
          }
        }
      }
    }
}

class MockHomePresenter : Presenter<HomeIntent, State> {
  private val stateRelay: Relay<State> = PublishRelay.create()

  fun emit(state: State) {
    stateRelay.accept(state)
  }

  override val events: Consumer<HomeIntent> = mock()
  override val states: Flowable<State> = stateRelay
    .startWith(State.Loading)
    .toFlowable(BackpressureStrategy.BUFFER)
}
