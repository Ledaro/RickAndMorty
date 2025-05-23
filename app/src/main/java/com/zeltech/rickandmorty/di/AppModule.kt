package com.zeltech.rickandmorty.di

import com.apollographql.apollo.ApolloClient
import com.zeltech.rickandmorty.features.characters.data.CharactersServiceImpl
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
            .build()
    }

    @Provides
    @Singleton
    fun provideCharactersService(apolloClient: ApolloClient): CharactersService {
        return CharactersServiceImpl(apolloClient)
    }
}
