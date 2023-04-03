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
    val isAlive: Boolean,
    val isDead: Boolean,
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
            val isAlive = preferences[PreferencesKeys.ALIVE_STATE] ?: false
            val isDead = preferences[PreferencesKeys.DEAD_STATE] ?: false
/*            val characterStatus = CharacterStatus.valueOf(
                preferences[PreferencesKeys.CHARACTER_STATUS] ?: CharacterStatus.ALL.name
            )*/

            FilterPreferences(isAlive, isDead)
        }

    suspend fun updateIsAlive(isAlive: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ALIVE_STATE] = isAlive
        }
    }

    suspend fun updateIsDead(isDead: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEAD_STATE] = isDead

        }
    }

/*    suspend fun updateCharacterStatus(characterStatus: CharacterStatus) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CHARACTER_STATUS] = characterStatus.name

        }
    }*/

    private object PreferencesKeys {
        val ALIVE_STATE = preferencesKey<Boolean>("alive_state")
        val DEAD_STATE = preferencesKey<Boolean>("dead_state")
/*        val CHARACTER_STATUS = preferencesKey<String>("character_status")*/
    }
}
