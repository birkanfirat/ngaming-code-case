package com.ngaming.ngamingcase.posts.domain.usecase

import com.ngaming.ngamingcase.posts.domain.model.Post
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Tek bir gönderiyi akış olarak veriyor. Gönderi silinirse null geliyor. */
class GetPostUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    operator fun invoke(id: Int): Flow<Post?> = repository.post(id)
}
