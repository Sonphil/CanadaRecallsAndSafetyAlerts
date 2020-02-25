package com.sonphil.canadarecallsandsafetyalerts.presentation

import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.applyThemePref
import com.sonphil.canadarecallsandsafetyalerts.ext.setVisible
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val navController by lazy { findNavController(R.id.fragment_nav_host_main) }
    /** Last destination selected **/
    private val _selectedDestinationId = MutableLiveData<Int>()
    val selectedDestinationId: LiveData<Int> = _selectedDestinationId

    private val topLevelDestinations = setOf(
        R.id.fragment_recent,
        R.id.fragment_my_recalls,
        R.id.fragment_more
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupTheme()
        setTheme(R.style.AppTheme)
        setContentView(binding.root)
        setupActionBar()
        setupBottomNavigation()
    }

    private fun setupTheme() {
        val darkThemePrefKey = getString(R.string.key_theme_pref)
        val darkThemePrefValue = sharedPreferences.getString(
            darkThemePrefKey,
            getString(R.string.value_default_theme_pref)
        )

        applyThemePref(darkThemePrefValue)
        setTheme(R.style.AppTheme)
    }

    private fun setupActionBar() {
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setSupportActionBar(binding.toolbar)
        binding.collapsingToolbarLayout.setupWithNavController(
            binding.toolbar,
            navController,
            appBarConfiguration
        )
    }

    private fun resetEmptyView() {
        with(binding.includeEmptyView) {
            tvTextEmpty.isVisible = false
            btnRetry.isVisible = false
            emptyView.isVisible = false
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            _selectedDestinationId.value = item.itemId

            if (item.itemId == navController.currentDestination?.id) {
                false
            } else {
                NavigationUI.onNavDestinationSelected(item, navController)

                true
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.appBarLayout.setExpanded(true, true)

            resetEmptyView()

            val shouldShowBottomNavigationView = destination.id in topLevelDestinations

            binding.bottomNavigationView.setVisible(shouldShowBottomNavigationView)
        }
    }
}
