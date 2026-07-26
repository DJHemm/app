package com.djhemm.loyaltycards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhemm.loyaltycards.model.LoyaltyCard
import com.djhemm.loyaltycards.repository.LoyaltyCardRepository
import kotlinx.coroutines.launch

class LoyaltyCardViewModel(private val repository: LoyaltyCardRepository) : ViewModel() {
    
    val allCards: LiveData<List<LoyaltyCard>> = repository.allCards
    val allCategories: LiveData<List<String>> = repository.getAllCategories()
    
    private var currentFilter: String? = null
    private var currentSearchQuery: String? = null
    
    fun getCardsByCategory(category: String): LiveData<List<LoyaltyCard>> {
        currentFilter = category
        currentSearchQuery = null
        return repository.getCardsByCategory(category)
    }
    
    fun searchCards(query: String): LiveData<List<LoyaltyCard>> {
        currentSearchQuery = query
        currentFilter = null
        return repository.searchCards(query)
    }
    
    fun insert(card: LoyaltyCard) = viewModelScope.launch {
        repository.insert(card)
    }
    
    fun update(card: LoyaltyCard) = viewModelScope.launch {
        repository.update(card)
    }
    
    fun delete(card: LoyaltyCard) = viewModelScope.launch {
        repository.delete(card)
    }
    
    fun getCardById(id: Long): LiveData<LoyaltyCard?> {
        return object : LiveData<LoyaltyCard?>() {
            override fun onActive() {
                super.onActive()
                viewModelScope.launch {
                    val card = repository.getCardById(id)
                    postValue(card)
                }
            }
        }
    }
}
