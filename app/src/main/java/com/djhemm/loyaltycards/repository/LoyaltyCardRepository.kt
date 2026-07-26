package com.djhemm.loyaltycards.repository

import androidx.lifecycle.LiveData
import com.djhemm.loyaltycards.model.LoyaltyCard
import com.djhemm.loyaltycards.model.LoyaltyCardDao

class LoyaltyCardRepository(private val loyaltyCardDao: LoyaltyCardDao) {
    
    val allCards: LiveData<List<LoyaltyCard>> = loyaltyCardDao.getAllCards()
    
    fun getCardsByCategory(category: String): LiveData<List<LoyaltyCard>> {
        return loyaltyCardDao.getCardsByCategory(category)
    }
    
    fun searchCards(query: String): LiveData<List<LoyaltyCard>> {
        return loyaltyCardDao.searchCards("%$query%")
    }
    
    suspend fun insert(card: LoyaltyCard): Long {
        return loyaltyCardDao.insert(card)
    }
    
    suspend fun update(card: LoyaltyCard) {
        loyaltyCardDao.update(card)
    }
    
    suspend fun delete(card: LoyaltyCard) {
        loyaltyCardDao.delete(card)
    }
    
    suspend fun getCardById(id: Long): LoyaltyCard? {
        return loyaltyCardDao.getCardById(id)
    }
    
    fun getAllCategories(): LiveData<List<String>> {
        return loyaltyCardDao.getAllCategories()
    }
}
