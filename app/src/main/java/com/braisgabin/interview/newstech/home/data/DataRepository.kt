package com.braisgabin.interview.newstech.home.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.braisgabin.interview.newstech.home.domain.Repository
import com.braisgabin.interview.newstech.entity.Photo
import io.reactivex.Flowable

class DataRepository(
  private val dataSource: ApiDataSource
) : Repository {

  override fun photos(): Flowable<Either<Throwable, List<Photo>>> {
    return dataSource.photos()
      .scan(emptyList<Photo>()) { list, photo ->
        /*
         * I know that I'm creating a new list every time that I receive a photo. A solution to fix
         * this is to use the operators buffer. I don't implement it because I don't want to
         * increase the complexity of the code just for 5k of lists.
         *
         * Premature optimization is the root of all evil.
         */
        list.plus(photo)
      }
      .skip(1) // I don't want to emit the empty list
      .defaultIfEmpty(emptyList()) // But I need to emit it if there's nothing else
      .map<Either<Throwable, List<Photo>>> { it.right() }
      .onErrorReturn { it.left() }
  }
}
