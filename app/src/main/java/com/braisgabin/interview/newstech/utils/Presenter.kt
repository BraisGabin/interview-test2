package com.braisgabin.interview.newstech.utils

import io.reactivex.Flowable
import io.reactivex.functions.Consumer

interface Presenter<Intent, State> {

  val events: Consumer<Intent>
  val states: Flowable<State>
}
