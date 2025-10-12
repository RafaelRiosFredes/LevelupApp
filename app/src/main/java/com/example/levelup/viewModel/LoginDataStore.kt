package com.example.levelup.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "preferencias_login")

class LoginDataStore(private val context: Context) {

    private val SESSION_ACTIVE = booleanPreferencesKey("session_active")

    suspend fun guardarSesionActiva (valor: Boolean){
        context.dataStore.edit { preferences ->
            preferences[SESSION_ACTIVE] = valor
        }
    }

    fun obtenerSesionActiva(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[SESSION_ACTIVE] ?: false
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences[SESSION_ACTIVE] = false
        }
    }
}
