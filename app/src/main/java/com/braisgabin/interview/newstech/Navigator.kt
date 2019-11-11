package com.braisgabin.interview.newstech

import android.app.Activity
import com.braisgabin.interview.newstech.detail.presentation.DetailActivity
import com.braisgabin.interview.newstech.utils.Mockable
import javax.inject.Inject

@Mockable
class Navigator @Inject constructor(
  private val activity: Activity
) {

  fun goToDetail(url: String) {
    activity.startActivity(DetailActivity.getCallingIntent(activity, url))
  }
}
