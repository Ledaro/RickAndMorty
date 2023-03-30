package com.example.rickandmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmorty.model.Character

@Database(entities = [Character::class], version = 1)
@TypeConverters(Converters::class)
abstract class CharactersDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}