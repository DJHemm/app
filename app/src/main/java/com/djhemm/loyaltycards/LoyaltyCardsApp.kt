package com.djhemm.loyaltycards

import android.app.Application
import com.djhemm.loyaltycards.model.AppDatabase
import com.djhemm.loyaltycards.repository.LoyaltyCardRepository

class LoyaltyCardsApp : Application() {
    
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { LoyaltyCardRepository(database.loyaltyCardDao()) }
}
