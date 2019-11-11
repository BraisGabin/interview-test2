package com.braisgabin.interview.newstech.home.presentation.feature

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.braisgabin.interview.newstech.entity.Photo
import com.braisgabin.interview.newstech.home.data.photo
import com.braisgabin.interview.newstech.home.domain.PhotosUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Test

class HomeActorTest {
  private val testScheduler = TestScheduler()

  private val useCase: PhotosUseCase = mock()
  private val actor = HomeActor(useCase, testScheduler)

  @After
  fun tearDown() {
    verifyNoMoreInteractions(useCase)
  }

  @Test
  fun load() {
    whenever(useCase.photos()).doReturn(
      Flowable.just<Either<Throwable, List<Photo>>>(
        listOf(photo("1")).right(),
        listOf(photo("1"), photo("2")).right()
      )
    )

    actor.invoke(State.Loading, Action.Reload)
      .cast(Effect::class.java)
      .test()
      .also { testScheduler.triggerActions() }
      .assertValues(
        Effect.Loading,
        Effect.Data(listOf(photo("1"), photo("2"))),
        Effect.NoMoreItems
      )

    verify(useCase).photos()
  }

  @Test
  fun load_error() {
    whenever(useCase.photos()).doReturn(
      Flowable.just<Either<Throwable, List<Photo>>>(
        Throwable().left()
      )
    )

    actor.invoke(State.Loading, Action.Reload)
      .cast(Effect::class.java)
      .test()
      .also { testScheduler.triggerActions() }
      .assertValues(
        Effect.Loading,
        Effect.Error,
        Effect.NoMoreItems
      )

    verify(useCase).photos()
  }

  @Test
  fun load_empty() {
    whenever(useCase.photos()).doReturn(Flowable.empty())

    actor.invoke(State.Loading, Action.Reload)
      .cast(Effect::class.java)
      .test()
      .also { testScheduler.triggerActions() }
      .assertValues(
        Effect.Loading,
        Effect.Data(emptyList()),
        Effect.NoMoreItems
      )

    verify(useCase).photos()
  }
}
