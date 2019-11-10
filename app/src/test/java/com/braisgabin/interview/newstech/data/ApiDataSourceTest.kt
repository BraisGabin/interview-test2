package com.braisgabin.interview.newstech.data

import com.braisgabin.interview.newstech.entity.Photo
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.moshi.Moshi
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Test
import retrofit2.Response
import java.nio.charset.Charset

class ApiDataSourceTest {

  private val restApi: RestApi = mock()
  private val moshi = Moshi.Builder().build()
  private val dataSource = ApiDataSource(restApi, moshi.adapter(PhotoMapper::class.java))

  @After
  fun tearDown() {
    verifyNoMoreInteractions(restApi)
  }

  @Test
  fun testPhotos() {
    val response = getResourceAsBuffer("response/photos.json")
      .readString(Charset.forName("UTF-8"))
      .toResponseBody("application/json".toMediaType())
    whenever(restApi.photos()).doReturn(Single.just(Response.success(response)))

    dataSource.photos()
      .test()
      .assertValues(
        Photo("url1", "thumbnail"),
        Photo("url2", "thumbnail")
      )
      .assertComplete()

    verify(restApi).photos()
  }

  @Test
  fun testPhotos_error() {
    val response = "".toResponseBody("application/json".toMediaType())
    whenever(restApi.photos()).doReturn(Single.just(Response.error(500, response)))

    dataSource.photos()
      .test()
      .assertError(Throwable::class.java)

    verify(restApi).photos()
  }
}
