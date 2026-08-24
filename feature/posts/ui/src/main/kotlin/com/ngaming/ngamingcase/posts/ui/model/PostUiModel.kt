package com.ngaming.ngamingcase.posts.ui.model

import com.ngaming.ngamingcase.posts.domain.model.Post

/** Gönderinin listede gösterilen hali. */
data class PostUiModel(
    val id: Int,
    val title: String,
    val body: String,
)

fun Post.toUiModel() = PostUiModel(
    id = id,
    title = title,
    body = body,
)

private const val IMAGE_URL_TEMPLATE = "https://picsum.photos/300/300?random=%d&grayscale"

/** Görsel adresini satırın pozisyonundan üretiyor. */
fun postImageUrl(itemPosition: Int): String = IMAGE_URL_TEMPLATE.format(itemPosition)
