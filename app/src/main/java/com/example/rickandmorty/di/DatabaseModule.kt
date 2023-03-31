package com.example.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.data.local.CharactersDatabase
import com.example.rickandmorty.data.local.Converters
import com.example.rickandmorty.util.GsonParser
import com.example.rickandmorty.util.Constants.Companion.CHARACTERS_DATABASE
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CharactersDatabase {
        return Room.databaseBuilder(
            context,
            CharactersDatabase::class.java,
            CHARACTERS_DATABASE
        ).addTypeConverter(Converters(GsonParser(Gson()))).build()
    }
}
