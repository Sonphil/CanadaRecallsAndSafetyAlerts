package com.sonphil.canadarecallsandsafetyalerts.presentation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sonphil.canadarecallsandsafetyalerts.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    private val navController by lazy { findNavController(R.id.fragment_nav_host_main) }
    /** Last destination selected **/
    private val _selectedDestinationId = MutableLiveData<Int>()
    val selectedDestinationId: LiveData<Int> = _selectedDestinationId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setupActionBar()
        setupBottomNavigation()
    }

    private fun setupActionBar() {
        val topLevelDestinations = setOf(
            R.id.fragment_recent,
            R.id.fragment_my_recalls,
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
        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            _selectedDestinationId.value = item.itemId

            if (item.itemId == navController.currentDestination?.id) {
                false
            } else {
                NavigationUI.onNavDestinationSelected(item, navController)

                true
            }
        }

        navController.addOnDestinationChangedListener { _, _, _ ->
            app_bar_layout.setExpanded(true, true)
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat?,
        pref: Preference?
    ): Boolean {
        navController.navigate(R.id.fragment_notifications)

        return true
    }
}
