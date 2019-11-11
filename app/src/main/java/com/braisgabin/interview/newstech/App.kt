package com.braisgabin.interview.newstech

import android.app.Application
import android.content.Context

class App : Application(), BaseApp {

  override val component: AppComponent by lazy {
    DaggerAppComponent.factory().create(BASE_URL)
  }
}

fun Context.appComponent(): AppComponent {
  return (this.applicationContext as BaseApp).component
}

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

interface BaseApp {
  val component: AppComponent
}
