package com.braisgabin.interview.newstech.data

import arrow.core.left
import arrow.core.right
import com.braisgabin.interview.newstech.entity.Photo
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Test

class DataRepositoryTest {

  private val apiDataSource: ApiDataSource = mock()
  private val repository = DataRepository(apiDataSource)

  @Test
  fun photos() {
    whenever(apiDataSource.photos()).doReturn(Flowable.just(photo("url1"), photo("url2")))

    repository.photos()
      .test()
      .assertValues(
        listOf(photo("url1")).right(),
        listOf(photo("url1"), photo("url2")).right()
      )
  }

  @Test
  fun photos_empty() {
    whenever(apiDataSource.photos()).doReturn(Flowable.empty())

    repository.photos()
      .test()
      .assertValues(
        emptyList<Photo>().right()
      )
  }

  @Test
  fun photos_error() {
    val throwable = Throwable()
    whenever(apiDataSource.photos()).doReturn(Flowable.error(throwable))

    repository.photos()
      .test()
      .assertValues(throwable.left())
  }
}

