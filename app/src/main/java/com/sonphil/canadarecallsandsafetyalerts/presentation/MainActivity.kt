package com.sonphil.canadarecallsandsafetyalerts.presentation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialContainerTransformSharedElementCallback
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.databinding.ActivityMainBinding
import com.sonphil.canadarecallsandsafetyalerts.ext.*
import com.sonphil.canadarecallsandsafetyalerts.worker.SyncRecallsWorkerScheduler
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
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
        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setTheme(R.style.AppTheme)
        setContentView(binding.root)
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

        binding.appBarLayout.doApplyTopInsetToTopMarginWhenAttached()
        binding.fragmentNavHostMain.doApplyInsetsWhenAttached { view, windowInsets ->
            view.updatePadding(bottom = view.paddingBottom + windowInsets.systemWindowInsetTop)
        }
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

            binding.bottomNavigationView.setVisible(shouldShowBottomNavigationView)
        }
    }
}
