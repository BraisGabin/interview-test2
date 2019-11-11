package com.braisgabin.interview.newstech.home.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import io.reactivex.Flowable
import org.junit.After
import org.junit.Test

class PhotosUseCaseTest {

  private val repository: Repository = mock {
    on { photos() } doReturn Flowable.empty()
  }
  private val useCase = PhotosUseCase(repository)

  @After
  fun tearDown() {
    verifyNoMoreInteractions(repository)
  }

  @Test
  fun name() {
    useCase.photos()
      .test()
      .assertNoValues()
      .assertComplete()

    verify(repository).photos()
  }
}
