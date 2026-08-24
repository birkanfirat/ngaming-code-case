package com.ngaming.ngamingcase.posts.ui.list

import com.ngaming.ngamingcase.core.common.AppError
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.ui.model.PostUiModel

/** Liste ekranının o anki durumu. */
data class PostListUiState(
    val posts: List<PostUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
) {
    val showEmptyState: Boolean get() = !isLoading && !isRefreshing && posts.isEmpty()
}

/** Bir kez gösterilip geçecek olaylar. Snackbar gibi. */
sealed interface PostListEvent {
    data class PostDeleted(val post: Post, val position: Int) : PostListEvent
    data class Failed(val error: AppError) : PostListEvent
}
