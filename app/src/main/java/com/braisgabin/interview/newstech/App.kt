package com.braisgabin.interview.newstech

import android.app.Application
import android.content.Context

class App : Application() {

  val component: AppComponent by lazy {
    DaggerAppComponent.factory().create(BASE_URL)
  }
}

fun Context.appComponent(): AppComponent {
  return (this.applicationContext as App).component
}

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
