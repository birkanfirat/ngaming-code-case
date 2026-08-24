package com.ngaming.ngamingcase.posts.domain.usecase

import com.ngaming.ngamingcase.core.common.AppResult
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import javax.inject.Inject

/** Listeyi API'den çekiyor. */
class RefreshPostsUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> = repository.refresh()
}
