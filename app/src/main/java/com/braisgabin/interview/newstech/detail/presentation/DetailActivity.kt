package com.braisgabin.interview.newstech.detail.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.braisgabin.interview.newstech.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    val url = intent.getStringExtra(EXTRA_URL)!!

    Picasso.get()
      .load(url)
      .centerInside()
      .fit()
      .into(imageView)
  }

  companion object {

    fun getCallingIntent(context: Context, url: String): Intent {
      return Intent(context, DetailActivity::class.java)
        .putExtra(EXTRA_URL, url)
    }
  }
}

private const val EXTRA_URL: String = "EXTRA_URL"
