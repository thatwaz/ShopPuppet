package com.thatwaz.shoppuppet.presentation.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

// u.i. design 09/23/2023 - 10/4/2023
// Official start date 10/5/2023

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        val toolbar: Toolbar = findViewById(R.id.app_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)  // Ensure up button is not displayed

        // Setup BottomNavigationView with NavController
        val bottomNavigationView = binding.bnvShopPuppet

        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Set the title of ActionBar based on the label of the current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addShopFragment -> {
                    // Hide the BottomNavigationView when in AddShopsFragment
                    binding.bnvShopPuppet.visibility = View.GONE
                    supportActionBar?.title = destination.label
                }
                R.id.tagItemToShopsFragment -> {
                    binding.bnvShopPuppet.visibility = View.GONE
                    supportActionBar?.show()
                    supportActionBar?.title = destination.label
                }
                R.id.shopSpecificListFragment -> {
                    // Hide the toolbar when in ShopSpecificListFragment
                    supportActionBar?.hide()
                    binding.bnvShopPuppet.visibility = View.GONE
                }
                R.id.addItemFragment -> {
                    supportActionBar?.show()
                    supportActionBar?.title = destination.label
                    binding.bnvShopPuppet.visibility = View.GONE
                }
                else -> {
                    // Show the BottomNavigationView and toolbar for all other fragments
                    binding.bnvShopPuppet.visibility = View.VISIBLE
                    supportActionBar?.show()
                    supportActionBar?.title = destination.label
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


}