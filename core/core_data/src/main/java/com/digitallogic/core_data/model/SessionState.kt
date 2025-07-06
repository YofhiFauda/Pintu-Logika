package com.digitallogic.core_data.model

data class SessionState(
    val isFirstTimeLaunch: Boolean = true,
    val isLoggedIn: Boolean = false,
    val role: String? = null,
    val userId: String? = null,
    val email: String? = null,
    val nama: String? = null,
    val kelas: String? = null,
    val uid: String? = null,
)
