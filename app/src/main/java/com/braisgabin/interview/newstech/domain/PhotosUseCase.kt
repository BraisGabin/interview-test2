package com.braisgabin.interview.newstech.domain

import arrow.core.Either
import com.braisgabin.interview.newstech.entity.Photo
import io.reactivex.Flowable

class PhotosUseCase(
  private val repository: Repository
) {

  fun photos(): Flowable<Either<Throwable, List<Photo>>> {
    return repository.photos()
  }
}
