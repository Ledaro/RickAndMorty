package com.example.rickandmorty.data.local

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters_table")
    fun getAllCharacters(): PagingSource<Int, com.example.rickandmorty.model.Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: com.example.rickandmorty.model.Character)

    @Delete
    suspend fun deleteCharacter(character: com.example.rickandmorty.model.Character)
}
