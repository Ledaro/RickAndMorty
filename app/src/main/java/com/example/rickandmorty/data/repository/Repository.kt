package com.example.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.rickandmorty.data.local.CharactersDatabase
import com.example.rickandmorty.data.paging.CharactersPagingSource
import com.example.rickandmorty.data.remote.Api
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val api: Api,
    private val database: CharactersDatabase,
) {

    //API
    fun getSearchResults(query: String, status:String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharactersPagingSource(api, query, status) }
        ).flow

    //DATABASE
    fun getSavedCharacters() = database.characterDao().getAllCharacters()

    suspend fun insertCharacter(character: com.example.rickandmorty.model.Character) =
        database.characterDao().insertCharacter(character)

    suspend fun deleteCharacter(character: com.example.rickandmorty.model.Character) =
        database.characterDao().deleteCharacter(character)
}
