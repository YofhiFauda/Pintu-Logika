package com.pika.pintulogika.ui.preauth.onboarding


import com.digitallogic.core_data.session.SessionManager
import kotlinx.coroutines.flow.Flow

class OnboardingRepository(private val sessionManager: SessionManager) {

    val isFirstTimeLaunch: Flow<Boolean> = sessionManager.isFirstTimeLaunch

    suspend fun completeOnboarding() {
        sessionManager.setFirstTimeLaunch(false)
    }
}