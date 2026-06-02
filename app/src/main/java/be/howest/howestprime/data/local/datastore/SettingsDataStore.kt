package be.howest.howestprime.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val IS_SUBSCRIBED = booleanPreferencesKey("is_subscribed")
    }

    val isSubscribed: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_SUBSCRIBED] ?: false
    }

    suspend fun setSubscribed() {
        dataStore.edit { prefs ->
            prefs[IS_SUBSCRIBED] = true
        }
    }
}
