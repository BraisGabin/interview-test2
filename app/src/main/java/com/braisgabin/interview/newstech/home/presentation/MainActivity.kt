package com.braisgabin.interview.newstech.home.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.braisgabin.interview.newstech.Navigator
import com.braisgabin.interview.newstech.R
import com.braisgabin.interview.newstech.appComponent
import com.braisgabin.interview.newstech.entity.Photo
import com.braisgabin.interview.newstech.home.presentation.feature.HomeFeature
import com.braisgabin.interview.newstech.home.presentation.feature.State
import com.braisgabin.interview.newstech.utils.Presenter
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : AppCompatActivity(), PhotoAdapter.Listener {

  @Inject
  lateinit var presenter: Presenter<HomeIntent, State>

  private val disposable = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    appComponent()
      .mainActivityComponent()
      .create(this)
      .inject(this)

    // spanCount = 3 but if we want to support tablets we should calculate this value instead of
    // hardcoding it.
    recyclerView.layoutManager = GridLayoutManager(this, 3)

    retry.setOnClickListener { presenter.events.accept(HomeIntent.Retry) }
    refresher.setOnRefreshListener { presenter.events.accept(HomeIntent.Refresh) }
  }

  override fun onStart() {
    super.onStart()

    disposable.add(presenter.states
      .subscribe(this::render) {
        throw RuntimeException(it)
      })
  }

  override fun onStop() {
    super.onStop()
    disposable.clear()
  }

  private fun render(state: State) {
    when (state) {
      State.Loading -> allGoneExcept(progressBar)
      State.Error -> allGoneExcept(retry)
      is State.Data -> {
        allGoneExcept(refresher)
        refresher.isRefreshing = state.refreshing
        val adapter = (recyclerView.adapter as? PhotoAdapter ?: PhotoAdapter(this))
        /*
         * I have the loading in the Data State but I'm not showing it right now.
         * To implement it properly I should add a new item at he last position in the Adapter
         * That element would be shown only if loading=true because the user should wait
         * until new images arrive.
         */
        adapter.submitList(state.photos)
        if (recyclerView.adapter !== adapter) {
          recyclerView.adapter = adapter
        }
      }
    }
  }

  override fun clickListener(photo: Photo) {
    presenter.events.accept(HomeIntent.PhotoSelected(photo))
  }

  private fun allGoneExcept(view: View) {
    progressBar.goneIfNot(view)
    retry.goneIfNot(view)
    refresher.goneIfNot(view)
  }

  private fun View.goneIfNot(view: View) {
    this.visibility = if (this == view) View.VISIBLE else View.GONE
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

    @Binds
    abstract fun presenterInterfaceProvider(presenter: HomePresenter): Presenter<HomeIntent, State>

    @Module
    companion object {

      @JvmStatic
      @Provides
      fun presenterProvider(
        activity: AppCompatActivity,
        homeFeature: Provider<HomeFeature>,
        navigator: Navigator
      ): HomePresenter {
        return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {

          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomePresenter(homeFeature.get(), navigator) as T
          }
        }).get(HomePresenter::class.java)
      }
    }
  }
}
