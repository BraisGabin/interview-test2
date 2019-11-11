package com.braisgabin.interview.newstech.home.data

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming

interface RestApi {

  @Streaming
  @GET("photos/")
  fun photos(): Single<Response<ResponseBody>>
}
