package com.ngaming.ngamingcase.posts.domain.usecase

import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import javax.inject.Inject

/** Gönderiyi siliyor. */
class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(post: Post): AppResult<Unit> = repository.delete(post)
}
