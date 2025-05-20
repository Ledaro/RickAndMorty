package com.zeltech.rickandmorty.common.data.model

import androidx.compose.ui.text.LinkAnnotation.Url
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
    val residents: List<Url>? = null,
    val url: String? = null,
)
