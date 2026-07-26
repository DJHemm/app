package com.djhemm.loyaltycards.databinding

import android.view.View
import androidx.viewbinding.ViewBinding
import com.djhemm.loyaltycards.R

class ActivityAddEditCardBinding private constructor(val root: View) : ViewBinding {
    val toolbar = root.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
    val storeName = root.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.storeName)
    val cardNumber = root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.cardNumber)
    val barcode = root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.barcode)
    val scanBarcodeButton = root.findViewById<com.google.android.material.button.MaterialButton>(R.id.scanBarcodeButton)
    val category = root.findViewById<com.google.android.material.textfield.MaterialAutoCompleteTextView>(R.id.category)
    val expiryDate = root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.expiryDate)
    val notes = root.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.notes)
    val saveButton = root.findViewById<com.google.android.material.button.MaterialButton>(R.id.saveButton)
    val cancelButton = root.findViewById<com.google.android.material.button.MaterialButton>(R.id.cancelButton)
}

object ActivityAddEditCardBindingInflater {
    fun inflate(inflater: android.view.LayoutInflater): ActivityAddEditCardBinding {
        return ActivityAddEditCardBinding(inflater.inflate(R.layout.activity_add_edit_card, null, false))
    }
}
