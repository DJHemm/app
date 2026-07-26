package com.djhemm.loyaltycards.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.djhemm.loyaltycards.R
import com.djhemm.loyaltycards.databinding.ActivityCardDetailBinding
import com.djhemm.loyaltycards.viewmodel.LoyaltyCardViewModel
import com.djhemm.loyaltycards.viewmodel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.text.SimpleDateFormat
import java.util.Locale

class CardDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCardDetailBinding
    private val viewModel: LoyaltyCardViewModel by viewModels {
        ViewModelFactory((application as LoyaltyCardsApp).repository)
    }
    
    private var cardId: Long = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        cardId = intent.getLongExtra("CARD_ID", -1)
        
        if (cardId != -1L) {
            loadCardData()
        }
        
        setupButtons()
    }
    
    private fun loadCardData() {
        viewModel.getCardById(cardId).observe(this) { card ->
            card?.let {
                binding.storeName.text = it.storeName
                binding.category.text = it.category
                binding.cardNumber.text = it.cardNumber ?: "No card number"
                
                it.barcode?.let { barcode ->
                    val barcodeBitmap = generateBarcode(barcode)
                    binding.barcodeImage.setImageBitmap(barcodeBitmap)
                    binding.barcodeText.text = barcode
                } ?: run {
                    binding.barcodeText.text = "No barcode"
                }
                
                it.expiryDate?.let { date ->
                    val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                    binding.expiryDate.text = dateFormat.format(date)
                } ?: run {
                    binding.expiryDate.text = "No expiry date"
                }
                
                binding.notes.text = it.notes ?: "No notes"
            }
        }
    }
    
    private fun setupButtons() {
        binding.editButton.setOnClickListener {
            val intent = Intent(this, AddEditCardActivity::class.java).apply {
                putExtra("CARD_ID", cardId)
            }
            startActivity(intent)
            finish()
        }
        
        binding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage("Are you sure you want to delete this card?")
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.getCardById(cardId).observe(this) { card ->
                        card?.let {
                            viewModel.delete(it)
                            Toast.makeText(this, "Card deleted", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }
    
    private fun generateBarcode(barcode: String): Bitmap? {
        return try {
            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix: BitMatrix = multiFormatWriter.encode(
                barcode,
                BarcodeFormat.CODE_128,
                800,
                200
            )
            
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                }
            }
            
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
