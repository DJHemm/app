package com.djhemm.loyaltycards.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.djhemm.loyaltycards.R
import com.djhemm.loyaltycards.databinding.ActivityAddEditCardBinding
import com.djhemm.loyaltycards.model.LoyaltyCard
import com.djhemm.loyaltycards.viewmodel.LoyaltyCardViewModel
import com.djhemm.loyaltycards.viewmodel.ViewModelFactory
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddEditCardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddEditCardBinding
    private val viewModel: LoyaltyCardViewModel by viewModels {
        ViewModelFactory((application as LoyaltyCardsApp).repository)
    }
    
    private var cardId: Long? = null
    private var currentCard: LoyaltyCard? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        cardId = intent.getLongExtra("CARD_ID", -1).takeIf { it != -1L }
        
        setupCategoryDropdown()
        setupDatePicker()
        setupScanButton()
        setupSaveButton()
        setupCancelButton()
        
        if (cardId != null) {
            loadCardData()
        }
    }
    
    private fun setupCategoryDropdown() {
        val categories = arrayOf("Grocery", "Retail", "Restaurant", "Coffee Shop", "Pharmacy", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.category.setAdapter(adapter)
    }
    
    private fun setupDatePicker() {
        binding.expiryDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.time
                
                val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                binding.expiryDate.setText(dateFormat.format(selectedDate))
            }, year, month, day).show()
        }
    }
    
    private fun setupScanButton() {
        binding.scanBarcodeButton.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setPrompt("Scan a barcode")
            integrator.setOrientationLocked(true)
            integrator.initiateScan()
        }
    }
    
    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            saveCard()
        }
    }
    
    private fun setupCancelButton() {
        binding.cancelButton.setOnClickListener {
            finish()
        }
    }
    
    private fun loadCardData() {
        cardId?.let { id ->
            viewModel.getCardById(id).observe(this) { card ->
                card?.let {
                    currentCard = it
                    binding.storeName.setText(it.storeName)
                    binding.cardNumber.setText(it.cardNumber)
                    binding.barcode.setText(it.barcode)
                    binding.category.setText(it.category, false)
                    it.expiryDate?.let { date ->
                        val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                        binding.expiryDate.setText(dateFormat.format(date))
                    }
                    binding.notes.setText(it.notes)
                }
            }
        }
    }
    
    private fun saveCard() {
        val storeName = binding.storeName.text.toString().trim()
        if (storeName.isBlank()) {
            Toast.makeText(this, "Please enter a store name", Toast.LENGTH_SHORT).show()
            return
        }
        
        val cardNumber = binding.cardNumber.text.toString().trim().takeIf { it.isNotBlank() }
        val barcode = binding.barcode.text.toString().trim().takeIf { it.isNotBlank() }
        val category = binding.category.text.toString().trim().takeIf { it.isNotBlank() } ?: "Other"
        val notes = binding.notes.text.toString().trim().takeIf { it.isNotBlank() }
        
        val expiryDate = try {
            val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
            dateFormat.parse(binding.expiryDate.text.toString().trim())
        } catch (e: Exception) {
            null
        }
        
        val card = currentCard?.copy(
            storeName = storeName,
            cardNumber = cardNumber,
            barcode = barcode,
            category = category,
            expiryDate = expiryDate,
            notes = notes
        ) ?: LoyaltyCard(
            storeName = storeName,
            cardNumber = cardNumber,
            barcode = barcode,
            category = category,
            expiryDate = expiryDate,
            notes = notes
        )
        
        if (cardId == null) {
            viewModel.insert(card)
        } else {
            viewModel.update(card)
        }
        
        Toast.makeText(this, "Card saved", Toast.LENGTH_SHORT).show()
        finish()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.let {
            if (it.contents != null) {
                binding.barcode.setText(it.contents)
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
