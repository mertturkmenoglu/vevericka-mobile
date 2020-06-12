@file:JvmName("ImageViewExtensions")
@file:Suppress("unused")

package io.github.mertturkmenoglu.vevericka.util

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadCircleImage(url: String) {
    Glide.with(this.context).load(url).apply(RequestOptions().circleCrop()).into(this)
}

fun ImageView.loadCircleImage(uri: Uri) {
    Glide.with(this.context).load(uri).apply(RequestOptions().circleCrop()).into(this)
}