package com.pika.halaman_materi.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "submodul_status")


class  ProgressPreferences (private val context: Context) {
    private fun statusKey(submodulId: String) = booleanPreferencesKey("submodul_$submodulId")

    suspend fun markAsRead(submodulId: String) {
        context.dataStore.edit { prefs ->
            prefs[statusKey(submodulId)] = true
        }
    }

    fun isRead(submodulId: String): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[statusKey(submodulId)] ?: false
        }
    }

    suspend fun getReadSubmodules(): Set<String> {
        return context.dataStore.data.first().asMap().mapNotNull {
            val key = it.key.name
            if (key.startsWith("submodul_") && it.value == true) {
                key.removePrefix("submodul_")
            } else null
        }.toSet()
    }


}


