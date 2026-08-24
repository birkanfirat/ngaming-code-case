package com.ngaming.ngamingcase.posts.data.repository

import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.core.common.map
import com.ngaming.ngamingcase.core.common.onFailure
import com.ngaming.ngamingcase.core.common.onSuccess
import com.ngaming.ngamingcase.core.network.apiCall
import com.ngaming.ngamingcase.posts.data.remote.PostApi
import com.ngaming.ngamingcase.posts.data.remote.toDomain
import com.ngaming.ngamingcase.posts.data.remote.toDto
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/** Listeyi bellekte tutan tek kaynak. Silme ve güncelleme önce burada oluyor, istek başarısız olursa geri alınıyor. API kalıcı yazma yapmadığı için böyle kuruldu. */
@Singleton
internal class PostRepositoryImpl @Inject constructor(
    private val api: PostApi,
) : PostRepository {

    private val cache = MutableStateFlow(emptyList<Post>())

    override val posts: Flow<List<Post>> = cache.asStateFlow()

    override fun post(id: Int): Flow<Post?> = cache
        .map { posts -> posts.firstOrNull { it.id == id } }
        .distinctUntilChanged()

    override suspend fun refresh(): AppResult<Unit> = apiCall { api.getPosts() }
        .map { dtos -> cache.value = dtos.map { it.toDomain() } }

    override suspend fun update(post: Post): AppResult<Post> =
        apiCall { api.updatePost(post.id, post.toDto()).toDomain() }
            .onSuccess { updated ->
                cache.update { posts -> posts.map { if (it.id == updated.id) updated else it } }
            }

    override suspend fun delete(post: Post): AppResult<Unit> {
        val position = cache.value.indexOfFirst { it.id == post.id }
        if (position == NOT_FOUND) return AppResult.Success(Unit)

        cache.update { posts -> posts.filterNot { it.id == post.id } }
        return apiCall { api.deletePost(post.id) }
            .onFailure { insert(post, position) }
    }

    override suspend fun restore(post: Post, position: Int) = insert(post, position)

    private fun insert(post: Post, position: Int) = cache.update { posts ->
        if (posts.any { it.id == post.id }) {
            posts
        } else {
            posts.toMutableList().apply { add(position.coerceIn(0, size), post) }
        }
    }

    private companion object {
        const val NOT_FOUND = -1
    }
}
