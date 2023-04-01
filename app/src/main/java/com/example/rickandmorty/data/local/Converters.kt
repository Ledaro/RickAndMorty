package com.example.rickandmorty.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.Origin
import com.example.rickandmorty.util.JsonParser
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {

    @TypeConverter
    fun fromEpisodes(json: String): List<String> {
        return jsonParser.fromJson<ArrayList<String>>(
            json,
            object : TypeToken<ArrayList<String>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toEpisodes(episodes: List<String>): String {
        return jsonParser.toJson(
            episodes,
            object : TypeToken<ArrayList<String>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromOrigin(origin: Origin): String {
        return origin.name
    }

    @TypeConverter
    fun toOrigin(name: String): Origin {
        return Origin(name, name)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return location.name
    }

    @TypeConverter
    fun toLocation(name: String): Location {
        return Location(name, name)
    }
}
