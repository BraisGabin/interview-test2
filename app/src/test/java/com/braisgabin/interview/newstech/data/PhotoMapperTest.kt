package com.braisgabin.interview.newstech.data

import com.braisgabin.interview.newstech.entity.Photo
import com.squareup.moshi.Moshi
import okio.BufferedSource
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PhotoMapperTest {

  private val moshi: Moshi = Moshi.Builder().build()
  private val jsonAdapter = moshi.adapter<PhotoMapper>(PhotoMapper::class.java)


  @Test
  fun toDomain() {
    val reader = getResourceAsBuffer("json/photo.json")
    val item = jsonAdapter.fromJson(reader)!!.toDomain()

    assertThat(
      item,
      `is`(
        Photo("https://url", "https://thumbnail")
      )
    )
  }
}

fun Any.getResourceAsBuffer(name: String): BufferedSource {
  return javaClass.classLoader!!.getResourceAsStream(name)!!.source().buffer()
}
