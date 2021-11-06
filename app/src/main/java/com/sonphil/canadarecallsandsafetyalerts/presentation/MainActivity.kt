package com.sonphil.canadarecallsandsafetyalerts.presentation

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.applyThemePref
import com.sonphil.canadarecallsandsafetyalerts.ext.doApplyInsetsToBottomPadding
import com.sonphil.canadarecallsandsafetyalerts.ext.doApplyTopInsetToTopMargin
import com.sonphil.canadarecallsandsafetyalerts.ext.doOnApplyWindowInsets
import com.sonphil.canadarecallsandsafetyalerts.ext.setVisible
import com.sonphil.canadarecallsandsafetyalerts.ext.viewBinding
import com.sonphil.canadarecallsandsafetyalerts.worker.SyncRecallsWorkerScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var syncRecallsWorkerScheduler: SyncRecallsWorkerScheduler
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_nav_host_main) as NavHostFragment)
            .findNavController()
    }
    /** Last destination selected **/
    private val _selectedTopLevelDestinationId = MutableLiveData<Int>()
    val selectedTopLevelDestinationId: LiveData<Int> = _selectedTopLevelDestinationId

    private val topLevelDestinations = setOf(
        R.id.fragment_recent,
        R.id.fragment_my_recalls,
        R.id.fragment_more
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTheme()
        setupSharedElementTransition()
        setTheme(R.style.Theme_App)
        setContentView(binding.root)
        setupWindow()

        // Workaround for the following issue: https://github.com/material-components/material-components-android/issues/1310
        // [CollapsingToolbarLayout] Consuming system window insets blocks sibling views from receiving insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.collapsingToolbarLayout, null)

        setupActionBar()
        setupBottomNavigation()
        syncRecallsWorkerScheduler.scheduleAccordingToPreferences()
    }

    private fun setupSharedElementTransition() {
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
    }

    private fun setupTheme() {
        val darkThemePrefKey = getString(R.string.key_theme_pref)
        val darkThemePrefValue = sharedPreferences.getString(
            darkThemePrefKey,
            getString(R.string.value_default_theme_pref)
        )

        applyThemePref(darkThemePrefValue)
        setTheme(R.style.Theme_App)
    }

    private fun setupWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun setupActionBar() {
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setSupportActionBar(binding.toolbar)
        binding.collapsingToolbarLayout.setupWithNavController(
            binding.toolbar,
            navController,
            appBarConfiguration
        )

        binding.appBarLayout.doApplyTopInsetToTopMargin()
        binding.fragmentNavHostMain.doApplyInsetsToBottomPadding()
    }

    private fun resetEmptyView() {
        with(binding.includeEmptyView) {
            tvTextEmpty.isVisible = false
            btnRetry.isVisible = false
            emptyView.isVisible = false
        }
    }

    private fun setupBottomNavigation() {
        val navigationView = binding.bottomNavigationView

        navigationView.setupWithNavController(navController)
        navigationView.setOnItemSelectedListener { item ->
            _selectedTopLevelDestinationId.value = item.itemId

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

            navigationView.setVisible(shouldShowBottomNavigationView)
        }

        navigationView.doOnApplyWindowInsets { view, windowInsets, initialPadding, _ ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updatePadding(bottom = initialPadding.bottom + insets.bottom)
        }
    }
}
