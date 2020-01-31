package com.sonphil.canadarecallsandsafetyalerts.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sonphil.canadarecallsandsafetyalerts.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val navController by lazy { findNavController(R.id.fragment_nav_host_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBar()
        setupBottomNavigation()
    }

    private fun setupActionBar() {
        val topLevelDestinations = setOf(R.id.fragment_recent, R.id.fragment_my_recalls)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNavigation() {
        bottom_navigation_view.setupWithNavController(navController)
    }
}
