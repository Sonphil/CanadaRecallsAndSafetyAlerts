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
import androidx.preference.ListPreference
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

        listOf(
            R.string.key_notifications_pref,
            R.string.key_notifications_sync_frequency_in_minutes_pref
        ).forEach { keyId ->
            val pref = findPreference<Preference>(getString(keyId))

            pref?.onPreferenceChangeListener = this
        }

        val notificationsPrefKey = getString(R.string.key_notifications_pref)
        val notificationsPref = preferenceScreen.findPreference<ListPreference>(notificationsPrefKey)
        if (notificationsPref != null) {
            enableOrDisableSyncFrequencyPref(notificationsPref.value)
        }
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
                navigateToSystemAppNotificationSettings()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToSystemAppNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            startActivity(intent)
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:${requireContext().packageName}")
            startActivity(intent)
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        return when (preference.key) {
            getString(R.string.key_notifications_pref) -> {
                handleNotificationsPreferenceChange(newValue)

                true
            }
            getString(R.string.key_notifications_sync_frequency_in_minutes_pref) -> {
                true
            }
            else -> false
        }
    }

    private fun handleNotificationsPreferenceChange(newValue: Any?) {
        if (newValue is String) {
            enableOrDisableSyncFrequencyPref(newValue)
        }
    }

    private fun enableOrDisableSyncFrequencyPref(notificationsPrefValue: String) {
        val syncFrequencyPrefKey = getString(R.string.key_notifications_sync_frequency_in_minutes_pref)
        val syncFrequencyPref = preferenceScreen.findPreference<Preference>(syncFrequencyPrefKey)

        val enabled = notificationsPrefValue != getString(R.string.value_notifications_pref_no)
        syncFrequencyPref?.isEnabled = enabled
    }
}
