package com.zeltech.rickandmorty.characters.data

import com.zeltech.CharacterQuery
import com.zeltech.rickandmorty.common.data.model.Character
import com.zeltech.rickandmorty.common.data.model.Episode
import com.zeltech.rickandmorty.common.data.model.Location

fun CharacterQuery.Character.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin?.toDomain(),
        location = location?.toDomain(),
        image = image,
        episode = episode.mapNotNull { it?.toDomain() },
        created = created
    )
}

private fun CharacterQuery.Origin.toDomain(): Location {
    return Location(id = id)
}

private fun CharacterQuery.Location.toDomain(): Location {
    return Location(id = id)
}

private fun CharacterQuery.Episode.toDomain(): Episode {
    return Episode(id = id)
}
