package com.example.rickandmorty.data.remote

import com.example.rickandmorty.model.CharactersResponse
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
