package com.djhemm.loyaltycards.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.djhemm.loyaltycards.R
import com.djhemm.loyaltycards.adapter.CardAdapter
import com.djhemm.loyaltycards.databinding.ActivityMainBinding
import com.djhemm.loyaltycards.model.LoyaltyCard
import com.djhemm.loyaltycards.viewmodel.LoyaltyCardViewModel
import com.djhemm.loyaltycards.viewmodel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CardAdapter
    private val viewModel: LoyaltyCardViewModel by viewModels {
        ViewModelFactory((application as LoyaltyCardsApp).repository)
    }
    
    // Keep track of current observer to avoid memory leaks
    private var currentCardsObserver: Observer<List<LoyaltyCard>>? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        setupRecyclerView()
        setupFab()
        observeData()
    }
    
    private fun setupRecyclerView() {
        adapter = CardAdapter { card ->
            val intent = Intent(this, CardDetailActivity::class.java).apply {
                putExtra("CARD_ID", card.id)
            }
            startActivity(intent)
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
    
    private fun setupFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddEditCardActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun observeData() {
        // Remove previous observer if exists
        currentCardsObserver?.let { viewModel.allCards.removeObserver(it) }
        
        currentCardsObserver = Observer { cards ->
            adapter.updateCards(cards)
        }
        
        viewModel.allCards.observe(this, currentCardsObserver!!)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchCards(it) }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchCards(it) }
                return true
            }
        })
        
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showCategoryFilter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun searchCards(query: String) {
        // Remove previous observer
        currentCardsObserver?.let { viewModel.allCards.removeObserver(it) }
        
        if (query.isBlank()) {
            currentCardsObserver = Observer { cards ->
                adapter.updateCards(cards)
            }
            viewModel.allCards.observe(this, currentCardsObserver!!)
        } else {
            currentCardsObserver = Observer { cards ->
                adapter.updateCards(cards)
            }
            viewModel.searchCards(query).observe(this, currentCardsObserver!!)
        }
    }
    
    private fun showCategoryFilter() {
        val categories = arrayOf("All Categories", "Grocery", "Retail", "Restaurant", "Coffee Shop", "Pharmacy", "Other")
        
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.all_categories)
            .setItems(categories) { _, which ->
                // Remove previous observer
                currentCardsObserver?.let { viewModel.allCards.removeObserver(it) }
                
                when (which) {
                    0 -> {
                        currentCardsObserver = Observer { cards ->
                            adapter.updateCards(cards)
                        }
                        viewModel.allCards.observe(this, currentCardsObserver!!)
                    }
                    else -> {
                        val category = when (which) {
                            1 -> "Grocery"
                            2 -> "Retail"
                            3 -> "Restaurant"
                            4 -> "Coffee Shop"
                            5 -> "Pharmacy"
                            else -> "Other"
                        }
                        currentCardsObserver = Observer { cards ->
                            adapter.updateCards(cards)
                        }
                        viewModel.getCardsByCategory(category).observe(this, currentCardsObserver!!)
                    }
                }
            }
            .show()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up observer to prevent memory leaks
        currentCardsObserver?.let { viewModel.allCards.removeObserver(it) }
        currentCardsObserver = null
    }
}
