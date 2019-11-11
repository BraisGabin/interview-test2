package com.braisgabin.interview.newstech

import android.os.Looper
import com.braisgabin.interview.newstech.home.data.DataRepository
import com.braisgabin.interview.newstech.home.data.PhotoMapper
import com.braisgabin.interview.newstech.home.data.RestApi
import com.braisgabin.interview.newstech.home.domain.Repository
import com.braisgabin.interview.newstech.home.presentation.MainActivity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

  @Component.Factory
  interface Factory {

    fun create(@BindsInstance @Named("baseUrl") baseUrl: String): AppComponent
  }

  fun mainActivityComponent(): MainActivity.Component.Factory
}

@Module
abstract class AppModule {

  @Binds
  abstract fun repositoryProvider(repository: DataRepository): Repository

  @Module
  companion object {

    @JvmStatic
    @Provides
    @Named("main")
    fun mainSchedulerProvider(): Scheduler = AndroidSchedulers.mainThread()

    @JvmStatic
    @Provides
    @Singleton
    fun okhttpProvider(): OkHttpClient {
      check(Looper.getMainLooper() != Looper.myLooper()) { "Initializing OkHttpClient on main thread." }
      val builder = OkHttpClient().newBuilder()
      if (BuildConfig.DEBUG) {
        builder.addNetworkInterceptor(HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BASIC
        })
      }
      return builder.build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun retrofitProvider(
      okHttpClient: Lazy<OkHttpClient>,
      @Named("baseUrl") baseUrl: String
    ): Retrofit {
      return Retrofit.Builder()
        .baseUrl(baseUrl)
        .callFactory(object : Call.Factory {
          override fun newCall(request: Request): Call {
            return okHttpClient.get().newCall(request)
          }
        })
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
    }

    @JvmStatic
    @Provides
    fun restApiProvider(retrofit: Retrofit): RestApi {
      return retrofit.create()
    }

    @JvmStatic
    @Provides
    @Reusable
    fun moshiProvider(): Moshi {
      return Moshi.Builder().build()
    }

    @JvmStatic
    @Provides
    fun photoJsonAdapterProvider(moshi: Moshi): JsonAdapter<PhotoMapper> {
      return moshi.adapter(PhotoMapper::class.java)
    }
  }
}
