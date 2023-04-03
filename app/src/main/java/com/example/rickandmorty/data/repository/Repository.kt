package com.example.rickandmorty.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
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
    fun getSearchResults(query: String, isAlive: Boolean, isDead: Boolean) =
        run {
            Log.d("REPO_CALL", "getSearchResults() function is triggered")
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    maxSize = 100,
                    enablePlaceholders = false,
                    prefetchDistance = 2
                ),
                pagingSourceFactory = {
                    CharactersPagingSource(
                        api,
                        query,
                        getStatusQueryParam(isAlive, isDead)
                    )
                }
            ).flow
        }

    private fun getStatusQueryParam(isAlive: Boolean, isDead: Boolean): String {
        return if (isAlive && !isDead) {
            STATUS_ALIVE
        } else if (!isAlive && isDead) {
            STATUS_DEAD
        } else STATUS_ALL
    }

    //DATABASE
    fun getSavedCharacters() = database.characterDao().getAllCharacters()

    suspend fun insertCharacter(character: com.example.rickandmorty.model.Character) =
        database.characterDao().insertCharacter(character)

    suspend fun deleteCharacter(character: com.example.rickandmorty.model.Character) =
        database.characterDao().deleteCharacter(character)
}
