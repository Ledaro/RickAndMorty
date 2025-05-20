package com.zeltech.rickandmorty.common.domain.model

data class CharactersResponse(
    val info: Info?,
    val results: List<Character>?
)
