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
import com.sonphil.canadarecallsandsafetyalerts.ext.applyThemePref
import com.sonphil.canadarecallsandsafetyalerts.ext.setVisible
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_empty_view.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
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

        setupTheme()
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
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

        setSupportActionBar(toolbar)
        collapsing_toolbar_layout.setupWithNavController(
            toolbar,
            navController,
            appBarConfiguration
        )
    }

    private fun resetEmptyView() {
        tv_text_empty.isVisible = false
        empty_view.isVisible = false
        btn_retry.isVisible = false
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            app_bar_layout.setExpanded(true, true)

            val shouldShowBottomNavigationView = destination.id in topLevelDestinations

            bottom_navigation_view.setVisible(shouldShowBottomNavigationView)

            resetEmptyView()
        }
    }
}
