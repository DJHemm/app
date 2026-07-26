package com.djhemm.loyaltycards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.djhemm.loyaltycards.repository.LoyaltyCardRepository

class ViewModelFactory(private val repository: LoyaltyCardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoyaltyCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoyaltyCardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
