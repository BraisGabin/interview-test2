package com.braisgabin.interview.newstech.home.data

import com.braisgabin.interview.newstech.entity.Photo
import okio.BufferedSource
import okio.buffer
import okio.source

fun Any.getResourceAsBuffer(name: String): BufferedSource {
  return javaClass.classLoader!!.getResourceAsStream(name)!!.source().buffer()
}

fun photo(
  id: String,
  url: String = "url",
  thumbnailUrl: String = "thumbnail"
) = Photo(id, url, thumbnailUrl)
