package com.braisgabin.interview.newstech.home.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.braisgabin.interview.newstech.R
import com.braisgabin.interview.newstech.entity.Photo
import com.squareup.picasso.Picasso

class PhotoAdapter(
  private val listener: Listener
) : ListAdapter<Photo, ViewHolder>(PhotoDiffer) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.create(parent, listener::clickListener)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  interface Listener {
    fun clickListener(photo: Photo)
  }
}

class ViewHolder(
  view: View,
  private val clickListener: (Photo) -> Unit
) : RecyclerView.ViewHolder(view) {

  private val imageView: ImageView = view.findViewById(R.id.imageView)

  fun bind(item: Photo) {
    itemView.setOnClickListener { clickListener(item) }

    Picasso.get()
      .load(item.thumbnailUrl)
      .fit()
      .centerCrop()
      .into(imageView)
  }

  companion object {
    fun create(
      parent: ViewGroup,
      clickListener: (Photo) -> Unit
    ): ViewHolder {
      return ViewHolder(
        LayoutInflater.from(parent.context).inflate(
          R.layout.item_photo,
          parent,
          false
        ), clickListener
      )
    }
  }
}

private object PhotoDiffer : DiffUtil.ItemCallback<Photo>() {
  override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
    return oldItem.thumbnailUrl == newItem.thumbnailUrl
  }
}
