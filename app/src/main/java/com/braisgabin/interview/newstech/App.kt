package com.braisgabin.interview.newstech

import android.app.Application
import android.content.Context

class App : Application() {

  val component: AppComponent by lazy {
    DaggerAppComponent.builder().build()
  }
}

fun Context.appComponent(): AppComponent {
  return (this.applicationContext as App).component
}
