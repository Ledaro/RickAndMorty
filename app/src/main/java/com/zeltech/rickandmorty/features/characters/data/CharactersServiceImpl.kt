package com.zeltech.rickandmorty.features.characters.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.zeltech.CharacterQuery
import com.zeltech.CharactersQuery
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.common.domain.model.CharactersResponse
import com.zeltech.rickandmorty.features.characters.data.mapper.toDomain
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import javax.inject.Inject

class CharactersServiceImpl
    @Inject
    constructor(
        private val client: ApolloClient,
    ) : CharactersService {
        override suspend fun getAllCharacters(): CharactersResponse? =
            client
                .query(CharactersQuery())
                .execute()
                .data
                ?.characters
                ?.toDomain()

        override suspend fun getFilteredCharacters(
            name: String?,
            status: String?,
            gender: String?,
        ): CharactersResponse? =
            client
                .query(
                    CharactersQuery(
                        Optional.presentIfNotNull(name),
                        Optional.presentIfNotNull(status),
                        Optional.presentIfNotNull(gender),
                    ),
                ).execute()
                .data
                ?.characters
                ?.toDomain()

        override suspend fun getCharacter(id: String): Character? =
            client
                .query(CharacterQuery(id))
                .execute()
                .data
                ?.character
                ?.toDomain()
    }
