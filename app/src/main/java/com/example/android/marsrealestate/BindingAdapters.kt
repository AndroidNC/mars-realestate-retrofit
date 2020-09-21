/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate

import android.opengl.Visibility
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.android.marsrealestate.network.MarsProperty
import com.example.android.marsrealestate.overview.MarsApiStatus
import com.example.android.marsrealestate.overview.PhotoGridAdapter
import kotlin.coroutines.coroutineContext

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                //.thumbnail(0.25f)
                .apply( RequestOptions()
                        //.centerCrop()
                        .placeholder(R.drawable.loading_animation)
                        //.transform(RoundedCorners(5))
                        //.diskCacheStrategy(DiskCacheStrategy.ALL) // It will cache your image after loaded for first time
                        .error(R.drawable.ic_broken_image))
                .into(imgView)

    }
}

@BindingAdapter("listData")
fun bindRecycleView(recyclerView: RecyclerView, data: List<MarsProperty>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("status")
fun bindImageStatus(imgView: ImageView, status:MarsApiStatus?) {
    status?.let {
        when(status) {
            MarsApiStatus.ERROR -> {
                imgView.visibility = View.VISIBLE
                imgView.setImageResource(R.drawable.ic_connection_error)
            }
            MarsApiStatus.LOADING -> {
                imgView.visibility = View.VISIBLE
                imgView.setImageResource(R.drawable.loading_animation)
            }
            MarsApiStatus.DONE -> {
                imgView.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("status")
fun bindTextViewStatus(txtView: TextView, status:MarsApiStatus?) {
    status?.let {
        when(status) {
            MarsApiStatus.ERROR -> {
                txtView.visibility = View.VISIBLE
            }
            MarsApiStatus.LOADING -> {
                txtView.visibility = View.GONE
            }
            MarsApiStatus.DONE -> {
                txtView.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("status")
fun bindButtonStatus(btn: Button, status:MarsApiStatus?) {
    status?.let {
        when(status) {
            MarsApiStatus.ERROR -> {
                btn.visibility = View.VISIBLE
            }
            MarsApiStatus.LOADING -> {
                btn.visibility = View.GONE
            }
            MarsApiStatus.DONE -> {
                btn.visibility = View.GONE
            }
        }
    }
}


