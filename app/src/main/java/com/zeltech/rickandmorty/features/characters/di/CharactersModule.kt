package com.zeltech.rickandmorty.features.characters.di

import com.zeltech.rickandmorty.features.characters.data.CharactersServiceImpl
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CharactersModule {

    @Binds
    abstract fun bindCharactersService(
        charactersServiceImpl: CharactersServiceImpl
    ): CharactersService
}
