package com.braisgabin.interview.newstech.home.data

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
    whenever(apiDataSource.photos()).doReturn(Flowable.just(photo("1"), photo("2")))

    repository.photos()
      .test()
      .assertValues(
        listOf(photo("1")).right(),
        listOf(photo("1"), photo("2")).right()
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

