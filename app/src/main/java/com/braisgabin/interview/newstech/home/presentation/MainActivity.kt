package com.braisgabin.interview.newstech.home.presentation

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.braisgabin.interview.newstech.R
import com.braisgabin.interview.newstech.appComponent
import com.braisgabin.interview.newstech.home.presentation.feature.HomeFeature
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var presenter: HomePresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    appComponent()
      .mainActivityComponent()
      .create(this)
      .inject(this)
  }

  @Subcomponent(modules = [MyModule::class])
  interface Component {

    @Subcomponent.Factory
    interface Factory {
      fun create(@BindsInstance activity: AppCompatActivity): Component
    }

    fun inject(activity: MainActivity)
  }

  @Module
  abstract class MyModule {

    @Binds
    abstract fun activityProvider(activity: AppCompatActivity): Activity

    @Module
    companion object {

      @JvmStatic
      @Provides
      fun presenterProvider(
        activity: AppCompatActivity,
        homeFeature: Provider<HomeFeature>
      ): HomePresenter {
        return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {

          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomePresenter(homeFeature.get()) as T
          }
        }).get(HomePresenter::class.java)
      }
    }
  }
}
