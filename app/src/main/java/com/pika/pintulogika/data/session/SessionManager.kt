package com.pika.pintulogika.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pika.pintulogika.data.model.SessionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.pika.pintulogika.utils.userDataStore


class SessionManager(private val context: Context) {

    /* ===== Key definitions ===== */
    private companion object {
        val IS_FIRST_TIME_LAUNCH = booleanPreferencesKey("is_first_time_launch")
        val IS_LOGGED_IN  = booleanPreferencesKey("is_logged_in")
        val USER_ROLE     = stringPreferencesKey("user_role")
        val USER_ID       = stringPreferencesKey("user_id")
        val EMAIL         = stringPreferencesKey("email")
        val NAMA          = stringPreferencesKey("nama")
        val KELAS         = stringPreferencesKey("kelas")
        val FIREBASE_UID           = stringPreferencesKey("firebase_uid")
    }

    // 🟢 ===== WRITE SESSION =====

    suspend fun setFirstTimeLaunch(isFirstTime: Boolean) = withContext(Dispatchers.IO) {
        context.userDataStore.edit { prefs ->
            prefs[IS_FIRST_TIME_LAUNCH] = isFirstTime
        }
    }

    /** Simpan data login GURU */
    suspend fun saveGuruSession(uid: String, email: String) = withContext(Dispatchers.IO) {
        context.userDataStore.edit {
            it[IS_LOGGED_IN] = true
            it[USER_ROLE] = "guru"
            it[USER_ID] = uid
            it[EMAIL] = email
            // Nama & kelas tidak relevan ⇒ kosongkan
            it.remove(NAMA); it.remove(KELAS)
        }
    }

    /** Simpan data login SISWA */
    suspend fun saveSiswaSession(nama: String, kelas: String, userId: String, uid: String? = null) =
        withContext(Dispatchers.IO) {
            context.userDataStore.edit {
                it[IS_LOGGED_IN] = true
                it[USER_ROLE] = "siswa"
                it[USER_ID] = userId
                it[NAMA] = nama
                it[KELAS] = kelas
                uid?.let { uidValue -> it[FIREBASE_UID] = uidValue }

                // Email tak perlu
                it.remove(EMAIL)
            }
        }

    //Clear Session saat button Keluar di tekan jadi tidak menghapus secara total
    suspend fun clearSession() = withContext(Dispatchers.IO) {
        context.userDataStore.edit {
            it[IS_LOGGED_IN] = false
            it[USER_ROLE] = ""
            it[USER_ID] = ""
            it[NAMA] = ""
            it[KELAS] = ""
            it[EMAIL] = ""
            it[FIREBASE_UID] = ""
            it[IS_FIRST_TIME_LAUNCH] = false  // opsional, tergantung kebutuhan onboarding
        }
    }


    /** Logout */
    suspend fun logout() = withContext(Dispatchers.IO) {
        context.userDataStore.edit { it.clear() }
    }

    /* ----------  READ section  ---------- */

    val isFirstTimeLaunch: Flow<Boolean> = context.userDataStore.data
        .map { prefs -> prefs[IS_FIRST_TIME_LAUNCH] ?: true }

    val isLoggedIn: Flow<Boolean> = context.userDataStore.data
        .map { it[IS_LOGGED_IN] ?: false }

    val sessionState: Flow<SessionState> = context.userDataStore.data
        .map { prefs ->
            SessionState(
                isFirstTimeLaunch = prefs[IS_FIRST_TIME_LAUNCH] ?: true,
                isLoggedIn        = prefs[IS_LOGGED_IN] ?: false,
                role              = prefs[USER_ROLE],
                userId            = prefs[USER_ID],
                email             = prefs[EMAIL],
                nama              = prefs[NAMA],
                kelas             = prefs[KELAS]

            )
        }.distinctUntilChanged()
}