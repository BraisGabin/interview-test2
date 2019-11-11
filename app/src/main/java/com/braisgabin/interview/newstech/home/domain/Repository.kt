package com.braisgabin.interview.newstech.home.domain

import arrow.core.Either
import com.braisgabin.interview.newstech.entity.Photo
import io.reactivex.Flowable

interface Repository {
  fun photos(): Flowable<Either<Throwable, List<Photo>>>
}
