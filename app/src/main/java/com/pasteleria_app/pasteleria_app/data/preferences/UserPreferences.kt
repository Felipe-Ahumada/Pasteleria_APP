package com.pasteleria_app.pasteleria_app.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_PHOTO = stringPreferencesKey("user_photo")
        private val KEY_USER_TOKEN = stringPreferencesKey("user_token")
        private val KEY_USER_ID = androidx.datastore.preferences.core.longPreferencesKey("user_id")
        private val KEY_USER_ROLE = stringPreferencesKey("user_role") // ✅ NUEVO
    }

    val userNameFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_NAME]
    }

    val userEmailFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_EMAIL]
    }

    val userPhotoFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_PHOTO]
    }

    val userTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_TOKEN]
    }

    val userIdFlow: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[KEY_USER_ID]
    }

    val userRoleFlow: Flow<String?> = context.dataStore.data.map { prefs -> // ✅ NUEVO
        prefs[KEY_USER_ROLE]
    }

    suspend fun saveUser(name: String, email: String, token: String, role: String) { // <-- Updated signature
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_NAME] = name
            prefs[KEY_USER_EMAIL] = email
            prefs[KEY_USER_TOKEN] = token
            prefs[KEY_USER_ROLE] = role // ✅ NUEVO
        }
    }

    suspend fun saveUserId(id: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = id
        }
    }

    suspend fun saveUserPhoto(uri: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_PHOTO] = uri
        }
    }

    suspend fun clearUserPhoto() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USER_PHOTO)
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USER_NAME)
            prefs.remove(KEY_USER_EMAIL)
            prefs.remove(KEY_USER_TOKEN)
            prefs.remove(KEY_USER_ID)
            prefs.remove(KEY_USER_ROLE) // ✅ NUEVO
            // ⚠️ No se borra la foto para mantenerla por usuario
        }
    }
}
