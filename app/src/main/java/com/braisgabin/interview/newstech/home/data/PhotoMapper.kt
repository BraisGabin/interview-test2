package com.braisgabin.interview.newstech.home.data

import com.braisgabin.interview.newstech.entity.Photo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PhotoMapper(
  val id: String,
  val url: String,
  val thumbnailUrl: String
) {

  fun toDomain(): Photo {
    return Photo(
      id,
      url,
      thumbnailUrl
    )
  }
}
