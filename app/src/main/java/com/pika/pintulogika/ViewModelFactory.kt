package com.pika.pintulogika


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pika.pintulogika.ui.preauth.onboarding.OnboardingRepository
import com.pika.pintulogika.ui.preauth.onboarding.OnboardingViewModel

class ViewModelFactory (
    private val repository: OnboardingRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}