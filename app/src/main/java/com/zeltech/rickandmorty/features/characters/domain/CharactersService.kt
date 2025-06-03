package com.zeltech.rickandmorty.features.characters.domain

import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.common.domain.model.CharactersResponse

interface CharactersService {
    suspend fun getAllCharacters(): CharactersResponse?

    suspend fun getFilteredCharacters(
        name: String?,
        status: String?,
        gender: String?,
    ): CharactersResponse?

    suspend fun getCharacter(id: String): Character?
}
