package com.pika.pintulogika.ui.preauth.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pika.pintulogika.data.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch


class OnboardingViewModel(private val repository: OnboardingRepository) : ViewModel() {

    val isFirstTimeLaunch: Flow<Boolean> = repository.isFirstTimeLaunch

    //TODO: STATE: untuk status UI yang terus berubah
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading

    //TODO: EVENT: untuk aksi sesaat
//    private val _uiEvent = MutableSharedFlow<UiEvent>()
//    val uiEvent: SharedFlow<UiEvent> = _uiEvent

    fun completeOnboarding() {
        viewModelScope.launch {
            repository.completeOnboarding()
        }
    }
}
