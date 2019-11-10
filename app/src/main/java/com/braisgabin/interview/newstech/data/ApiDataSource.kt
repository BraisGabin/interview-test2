package com.braisgabin.interview.newstech.data

import com.braisgabin.interview.newstech.entity.Photo
import com.braisgabin.interview.newstech.utils.Mockable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

@Mockable
class ApiDataSource(
  private val restApi: RestApi,
  private val jsonAdapter: JsonAdapter<PhotoMapper>
) {

  fun photos(): Flowable<Photo> {
    return restApi.photos()
      .flatMapPublisher {
        val source = it.body()?.source()
        if (source != null) {
          createFlowable(JsonReader.of(source))
        } else {
          Flowable.error(Throwable())
        }
      }
  }

  private fun createFlowable(reader: JsonReader): Flowable<Photo> {
    return Flowable.create<Photo>({ emitter ->
      reader.use {
        reader.beginArray()
        while (reader.hasNext() && !emitter.isCancelled) {
          val mapper = jsonAdapter.fromJson(reader)!!
          emitter.onNext(mapper.toDomain())
        }
        if (!emitter.isCancelled) {
          reader.endArray()
          emitter.onComplete()
        }
      }
    }, BackpressureStrategy.BUFFER)
  }
}
