package com.braisgabin.interview.newstech.data

import com.braisgabin.interview.newstech.entity.Photo
import okio.BufferedSource
import okio.buffer
import okio.source

fun Any.getResourceAsBuffer(name: String): BufferedSource {
  return javaClass.classLoader!!.getResourceAsStream(name)!!.source().buffer()
}

fun photo(
  url: String = "url",
  thumbnailUrl: String = "thumbnail"
) = Photo(url, thumbnailUrl)
