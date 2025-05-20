package com.zeltech.rickandmorty.features.characters.data.mapper

import com.zeltech.CharactersQuery
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.common.domain.model.CharactersResponse
import com.zeltech.rickandmorty.common.domain.model.Info

fun CharactersQuery.Characters.toDomain(): CharactersResponse {
    return CharactersResponse(
        info = info?.toDomain(),
        results = results?.mapNotNull { it?.toDomain() }
    )
}

fun CharactersQuery.Info.toDomain(): Info {
    return Info(
        count = count,
        pages = pages,
        next = next,
        prev = prev
    )
}

fun CharactersQuery.Result.toDomain(): Character {
    return Character(
        id = id
    )
}
