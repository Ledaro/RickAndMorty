package com.zeltech.rickandmorty.common.data.model

import androidx.compose.ui.text.LinkAnnotation.Url
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val id: String? = null,
    val name: String? = null,
    @SerialName("air_date")
    val airDate: String? = null,
    val episode: String? = null,
    val characters: List<Url>? = null,
    val url: String? = null,
    val created: String? = null
)
