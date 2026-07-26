package com.djhemm.loyaltycards.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.djhemm.loyaltycards.R
import com.djhemm.loyaltycards.databinding.ActivityCardDetailBinding
import com.djhemm.loyaltycards.model.LoyaltyCard
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
    private var currentCard: LoyaltyCard? = null
    
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
        viewModel.getCardById(cardId).observe(this, Observer { card ->
            card?.let {
                currentCard = it
                binding.storeName.text = it.storeName
                binding.category.text = it.category
                binding.cardNumber.text = it.cardNumber ?: getString(R.string.no_card_number)
                
                it.barcode?.let { barcode ->
                    val barcodeBitmap = generateBarcode(barcode)
                    if (barcodeBitmap != null) {
                        binding.barcodeImage.setImageBitmap(barcodeBitmap)
                    } else {
                        // Fallback: show barcode as text
                        binding.barcodeImage.setImageDrawable(null)
                    }
                    binding.barcodeText.text = barcode
                } ?: run {
                    binding.barcodeText.text = getString(R.string.no_barcode)
                }
                
                it.expiryDate?.let { date ->
                    val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                    binding.expiryDate.text = dateFormat.format(date)
                } ?: run {
                    binding.expiryDate.text = getString(R.string.no_expiry_date)
                }
                
                binding.notes.text = it.notes ?: getString(R.string.no_notes)
            }
        })
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
                .setMessage(getString(R.string.confirm_delete_message))
                .setPositiveButton(R.string.yes) { _, _ ->
                    currentCard?.let {
                        viewModel.delete(it)
                        Toast.makeText(this, R.string.card_deleted, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }
    
    private fun generateBarcode(barcode: String): Bitmap? {
        return try {
            // Try different barcode formats
            val formats = arrayOf(
                BarcodeFormat.CODE_128,
                BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_93,
                BarcodeFormat.UPC_A,
                BarcodeFormat.EAN_13,
                BarcodeFormat.ITF,
                BarcodeFormat.CODABAR
            )
            
            val multiFormatWriter = MultiFormatWriter()
            
            for (format in formats) {
                try {
                    val bitMatrix: BitMatrix = multiFormatWriter.encode(
                        barcode,
                        format,
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
                    
                    return bitmap
                } catch (e: Exception) {
                    // Try next format
                    continue
                }
            }
            
            // If all formats fail, try QR code as fallback
            val bitMatrix: BitMatrix = multiFormatWriter.encode(
                barcode,
                BarcodeFormat.QR_CODE,
                400,
                400
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
