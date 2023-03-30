package com.example.rickandmorty.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rickandmorty.model.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters_table")
    fun getAllCharacters(): LiveData<List<Character>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)
}
