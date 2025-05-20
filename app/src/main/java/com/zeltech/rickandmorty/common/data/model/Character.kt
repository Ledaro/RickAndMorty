package com.zeltech.rickandmorty.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String?,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val origin: Location?,
    val location: Location?,
    val image: String?,
    val episode: List<Episode>?,
    val created: String?
)
