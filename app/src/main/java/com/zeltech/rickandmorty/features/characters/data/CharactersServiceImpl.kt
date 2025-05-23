package com.zeltech.rickandmorty.features.characters.data

import com.apollographql.apollo.ApolloClient
import com.zeltech.CharacterQuery
import com.zeltech.CharactersQuery
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.common.domain.model.CharactersResponse
import com.zeltech.rickandmorty.features.characters.data.mapper.toDomain

class CharactersServiceImpl(
    private val client: ApolloClient
) : CharactersService {
    override suspend fun getAllCharacters(): CharactersResponse? {
        return client
            .query(CharactersQuery())
            .execute()
            .data
            ?.characters
            ?.toDomain()
    }

    override suspend fun getCharacter(id: String): Character? {
        return client
            .query(CharacterQuery(id))
            .execute()
            .data
            ?.character
            ?.toDomain()
    }
}
