package com.example.rickandmorty.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class CharacterStatus { ALIVE, DEAD, ALL }

private const val TAG = "PreferencesManager"

data class FilterPreferences(
    val statusAlive: Boolean,
    val statusDead: Boolean,
/*    val characterStatus: CharacterStatus*/
)

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext
    context: Context
) {
    private val dataStore = context.createDataStore("user_preferences")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val statusAlive = preferences[PreferencesKeys.STATUS_ALIVE] ?: false
            val statusDead = preferences[PreferencesKeys.STATUS_DEAD] ?: false
/*            val characterStatus =
                preferences[PreferencesKeys.CHARACTER_STATUS] ?: CharacterStatus.ALL*/
            FilterPreferences(statusAlive, statusDead)
        }

    suspend fun updateStatusAlive(statusAlive: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.STATUS_ALIVE] = statusAlive
        }
    }

    suspend fun updateStatusDead(statusDead: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.STATUS_DEAD] = statusDead

        }
    }

/*    suspend fun updateCharacterStatus(characterStatus: CharacterStatus) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CHARACTER_STATUS] = characterStatus

        }*/
}

private object PreferencesKeys {
    val STATUS_ALIVE = preferencesKey<Boolean>("status_alive")
    val STATUS_DEAD = preferencesKey<Boolean>("status_dead")
/*        val CHARACTER_STATUS = preferencesKey<CharacterStatus>("character_status")*/
}

