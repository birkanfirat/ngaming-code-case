package com.ngaming.ngamingcase.posts.ui.detail

import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.posts.domain.model.Post

/** Detay ekranının o anki durumu. */
data class PostDetailUiState(
    val post: Post? = null,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
)

/** Detay ekranında bir kez gösterilecek olaylar. */
sealed interface PostDetailEvent {
    data object Saved : PostDetailEvent

    data object Unchanged : PostDetailEvent

    data class Failed(val error: AppError) : PostDetailEvent
}
