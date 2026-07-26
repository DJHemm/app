package com.djhemm.loyaltycards.databinding

import android.view.View
import androidx.viewbinding.ViewBinding
import com.djhemm.loyaltycards.R

class ActivityCardDetailBinding private constructor(val root: View) : ViewBinding {
    val toolbar = root.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
    val storeName = root.findViewById<android.widget.TextView>(R.id.storeName)
    val category = root.findViewById<android.widget.TextView>(R.id.category)
    val cardNumber = root.findViewById<android.widget.TextView>(R.id.cardNumber)
    val barcodeImage = root.findViewById<android.widget.ImageView>(R.id.barcodeImage)
    val barcodeText = root.findViewById<android.widget.TextView>(R.id.barcodeText)
    val expiryDate = root.findViewById<android.widget.TextView>(R.id.expiryDate)
    val notes = root.findViewById<android.widget.TextView>(R.id.notes)
    val editButton = root.findViewById<com.google.android.material.button.MaterialButton>(R.id.editButton)
    val deleteButton = root.findViewById<com.google.android.material.button.MaterialButton>(R.id.deleteButton)
}

object ActivityCardDetailBindingInflater {
    fun inflate(inflater: android.view.LayoutInflater): ActivityCardDetailBinding {
        return ActivityCardDetailBinding(inflater.inflate(R.layout.activity_card_detail, null, false))
    }
}
