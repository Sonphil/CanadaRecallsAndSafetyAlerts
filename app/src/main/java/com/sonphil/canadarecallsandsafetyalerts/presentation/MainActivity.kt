package com.sonphil.canadarecallsandsafetyalerts.presentation

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {
    private val navController by lazy { findNavController(R.id.fragment_nav_host_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupActionBar()
        setupBottomNavigation()
    }

    private fun setupActionBar() {
        val topLevelDestinations = setOf(
            R.id.fragment_recent,
            R.id.fragment_my_recalls,
            R.id.fragment_notifications,
            R.id.fragment_more
        )
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setSupportActionBar(toolbar)
        collapsing_toolbar_layout.setupWithNavController(
            toolbar,
            navController,
            appBarConfiguration
        )
    }

    private fun setupBottomNavigation() {
        bottom_navigation_view.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, _ ->
            app_bar_layout.setExpanded(true, true)
        }
    }
}
