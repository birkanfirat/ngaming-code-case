package com.ngaming.ngamingcase.posts.data.di

import com.ngaming.ngamingcase.posts.data.remote.PostApi
import com.ngaming.ngamingcase.posts.data.repository.PostRepositoryImpl
import com.ngaming.ngamingcase.posts.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/** PostApi'yi Retrofit üzerinden üretiyor. */
@Module
@InstallIn(SingletonComponent::class)
internal object PostsRemoteModule {

    @Provides
    @Singleton
    fun providePostApi(retrofit: Retrofit): PostApi = retrofit.create(PostApi::class.java)
}

/** PostRepository istendiğinde implementasyonu veriyor. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class PostsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository
}
