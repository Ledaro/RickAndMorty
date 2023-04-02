package com.example.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickandmorty.data.datastore.CharacterStatus
import com.example.rickandmorty.data.local.CharactersDatabase
import com.example.rickandmorty.data.paging.CharactersPagingSource
import com.example.rickandmorty.data.remote.Api
import com.example.rickandmorty.util.Constants.Companion.STATUS_ALIVE
import com.example.rickandmorty.util.Constants.Companion.STATUS_ALL
import com.example.rickandmorty.util.Constants.Companion.STATUS_DEAD
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val api: Api,
    private val database: CharactersDatabase,
) {
    //API
    fun getSearchResults(query: String, status: CharacterStatus) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharactersPagingSource(
                    api,
                    query,
                    getStatusQueryParam(status)
                )
            }
        ).flow

    private fun getStatusQueryParam(status: CharacterStatus) =
        when (status) {
            CharacterStatus.ALIVE -> STATUS_ALIVE
            CharacterStatus.DEAD -> STATUS_DEAD
            CharacterStatus.ALL -> STATUS_ALL
        }

    //DATABASE
    fun getSavedCharacters() = database.characterDao().getAllCharacters()

    suspend fun insertCharacter(character: com.example.rickandmorty.model.Character) =
        database.characterDao().insertCharacter(character)

    suspend fun deleteCharacter(character: com.example.rickandmorty.model.Character) =
        database.characterDao().deleteCharacter(character)
}
