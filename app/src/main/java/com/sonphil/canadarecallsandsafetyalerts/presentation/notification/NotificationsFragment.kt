package com.sonphil.canadarecallsandsafetyalerts.presentation.notification

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sonphil.canadarecallsandsafetyalerts.R
import kotlinx.android.synthetic.main.activity_main.*

class NotificationsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().setTheme(R.style.AppTheme)

        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        addPreferencesFromResource(R.xml.preferences_notification)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().iv_section_icon.setImageResource(R.drawable.ic_notifications_black_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notifications, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_notifications_settings -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                    startActivity(intent)
                } else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${requireContext().packageName}")
                    startActivity(intent)
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        return false
    }
}
