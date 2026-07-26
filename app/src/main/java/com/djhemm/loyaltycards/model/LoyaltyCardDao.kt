package com.djhemm.loyaltycards.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LoyaltyCardDao {
    @Insert
    suspend fun insert(card: LoyaltyCard): Long

    @Update
    suspend fun update(card: LoyaltyCard)

    @Delete
    suspend fun delete(card: LoyaltyCard)

    @Query("SELECT * FROM loyalty_cards ORDER BY storeName ASC")
    fun getAllCards(): LiveData<List<LoyaltyCard>>

    @Query("SELECT * FROM loyalty_cards WHERE category = :category ORDER BY storeName ASC")
    fun getCardsByCategory(category: String): LiveData<List<LoyaltyCard>>

    @Query("SELECT * FROM loyalty_cards WHERE storeName LIKE :query OR cardNumber LIKE :query OR barcode LIKE :query ORDER BY storeName ASC")
    fun searchCards(query: String): LiveData<List<LoyaltyCard>>

    @Query("SELECT * FROM loyalty_cards WHERE id = :id")
    suspend fun getCardById(id: Long): LoyaltyCard?

    @Query("SELECT DISTINCT category FROM loyalty_cards ORDER BY category ASC")
    fun getAllCategories(): LiveData<List<String>>
    
    @Query("SELECT * FROM loyalty_cards WHERE storeName LIKE '%' || :query || '%' OR cardNumber LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%' ORDER BY storeName ASC")
    fun searchCardsSafe(query: String): LiveData<List<LoyaltyCard>>
}
