@file:JvmName("ViewExtensions")
@file:Suppress("unused")

package io.github.mertturkmenoglu.vevericka.util

import android.view.View

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeClickable() {
    isClickable = true
}

fun View.makeNonClickable() {
    isClickable = false
}