package com.zeltech.rickandmorty.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin: Location? = null,
    val location: Location? = null,
    val image: String? = null,
    val episode: List<Episode>? = null,
    val created: String? = null
)
