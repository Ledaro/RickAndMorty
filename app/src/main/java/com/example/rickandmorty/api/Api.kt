package com.example.rickandmorty.api

import com.example.rickandmorty.data.models.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("api/character")
    suspend fun getAllCharacters(
        @Query("page") page: Int
    ): CharactersResponse

    @GET("api/character")
    suspend fun searchCharacter(
        @Query("page") page: Int,
        @Query("name") query: String

    ): CharactersResponse
}
