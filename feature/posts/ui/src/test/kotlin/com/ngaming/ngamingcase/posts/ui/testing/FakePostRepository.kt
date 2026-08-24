package com.ngaming.ngamingcase.posts.ui.testing

import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/** Testler için sahte repository. Ağ yok, davranış aynı. */
class FakePostRepository : PostRepository {

    private val state = MutableStateFlow(emptyList<Post>())

    var remotePosts: List<Post> = emptyList()
    var refreshResult: AppResult<Unit> = AppResult.Success(Unit)

    override val posts: Flow<List<Post>> = state.asStateFlow()

    override fun post(id: Int): Flow<Post?> = state.map { posts -> posts.firstOrNull { it.id == id } }

    override suspend fun refresh(): AppResult<Unit> = refreshResult.also {
        if (it is AppResult.Success) state.value = remotePosts
    }

    override suspend fun update(post: Post): AppResult<Post> {
        state.value = state.value.map { if (it.id == post.id) post else it }
        return AppResult.Success(post)
    }

    override suspend fun delete(post: Post): AppResult<Unit> {
        state.value = state.value.filterNot { it.id == post.id }
        return AppResult.Success(Unit)
    }

    override suspend fun restore(post: Post, position: Int) {
        state.value = state.value.toMutableList().apply { add(position.coerceIn(0, size), post) }
    }
}
