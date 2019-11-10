package com.braisgabin.interview.newstech.data

import okio.BufferedSource
import okio.buffer
import okio.source


fun Any.getResourceAsBuffer(name: String): BufferedSource {
  return javaClass.classLoader!!.getResourceAsStream(name)!!.source().buffer()
}
