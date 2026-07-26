package com.djhemm.loyaltycards.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "loyalty_cards")
data class LoyaltyCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeName: String,
    val cardNumber: String? = null,
    val barcode: String? = null,
    val category: String = "Other",
    val expiryDate: Date? = null,
    val notes: String? = null,
    val createdAt: Date = Date()
)
