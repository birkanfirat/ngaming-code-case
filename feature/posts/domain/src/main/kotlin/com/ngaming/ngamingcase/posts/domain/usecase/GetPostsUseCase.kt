package com.ngaming.ngamingcase.posts.domain.usecase

import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Gönderi listesini akış olarak veriyor. */
class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    operator fun invoke(): Flow<List<Post>> = repository.posts
}
