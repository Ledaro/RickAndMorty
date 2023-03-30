package com.example.rickandmorty.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmorty.util.Constants.Companion.CHARACTERS_TABLE
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName = CHARACTERS_TABLE)
@Parcelize
data class Character(
    val created: String,
    val episode: List<String>,
    val gender: String,
    @PrimaryKey val id: Int? = null,
    val image: String,
    val location: @RawValue Location,
    val name: String,
    val origin: @RawValue Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) : Parcelable
