package com.braisgabin.interview.newstech.home.domain

import arrow.core.Either
import com.braisgabin.interview.newstech.entity.Photo
import com.braisgabin.interview.newstech.utils.Mockable
import io.reactivex.Flowable
import javax.inject.Inject

@Mockable
class PhotosUseCase @Inject constructor(
  private val repository: Repository
) {

  fun photos(): Flowable<Either<Throwable, List<Photo>>> {
    return repository.photos()
  }
}
