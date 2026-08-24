package com.ngaming.ngamingcase.core.ui.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ngaming.ngamingcase.core.ui.R

/** Görseli Glide ile yüklüyor, gelene kadar placeholder gösteriyor. */
fun ImageView.loadThumbnail(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.bg_thumbnail_placeholder)
        .into(this)
}

/** Bekleyen isteği iptal ediyor. Geri dönüşen satırda önceki görsel görünmesin diye. */
fun ImageView.clearThumbnail() {
    Glide.with(this).clear(this)
}
