package com.djhemm.loyaltycards.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    
    // Cache for single card lookups
    private val cardCache = mutableMapOf<Long, LoyaltyCard>()
    
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
        val newId = repository.insert(card)
        // Update cache with new ID
        cardCache[newId] = card.copy(id = newId)
    }
    
    fun update(card: LoyaltyCard) = viewModelScope.launch {
        repository.update(card)
        // Update cache
        cardCache[card.id] = card
    }
    
    fun delete(card: LoyaltyCard) = viewModelScope.launch {
        repository.delete(card)
        // Remove from cache
        cardCache.remove(card.id)
    }
    
    fun getCardById(id: Long): LiveData<LoyaltyCard?> {
        // Check cache first
        if (cardCache.containsKey(id)) {
            val liveData = MutableLiveData<LoyaltyCard?>()
            liveData.value = cardCache[id]
            return liveData
        }
        
        return object : LiveData<LoyaltyCard?>() {
            override fun onActive() {
                super.onActive()
                viewModelScope.launch {
                    val card = repository.getCardById(id)
                    card?.let { cardCache[id] = it }
                    postValue(card)
                }
            }
        }
    }
    
    fun getCardByIdSync(id: Long): LoyaltyCard? {
        return cardCache[id]
    }
}
