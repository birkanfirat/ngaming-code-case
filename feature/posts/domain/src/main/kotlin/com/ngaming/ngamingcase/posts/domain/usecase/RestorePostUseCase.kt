package com.ngaming.ngamingcase.posts.domain.usecase

import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import javax.inject.Inject

/** Silinen gönderiyi eski sırasına geri koyuyor. */
class RestorePostUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(post: Post, position: Int) = repository.restore(post, position)
}
