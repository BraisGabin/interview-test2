package com.braisgabin.interview.newstech.home.data

import com.braisgabin.interview.newstech.entity.Photo
import com.squareup.moshi.Moshi
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
