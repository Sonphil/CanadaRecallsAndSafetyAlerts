package com.sonphil.canadarecallsandsafetyalerts.presentation.more.preference

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.ext.applyThemePref
import com.sonphil.canadarecallsandsafetyalerts.worker.SyncRecallsWorker
import com.sonphil.canadarecallsandsafetyalerts.worker.SyncRecallsWorkerScheduler
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PreferenceFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    @Inject
    lateinit var syncRecallsWorkerScheduler: SyncRecallsWorkerScheduler

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)

        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
        addPreferencesFromResource(R.xml.preferences)

        setupChangeListener()

        val notificationsPrefKey = getString(R.string.key_notifications_pref)
        val notificationsPref =
            preferenceScreen.findPreference<ListPreference>(notificationsPrefKey)
        if (notificationsPref != null) {
            enableOrDisableNotificationsPreferences(notificationsPref.value)
        }

        setupNotificationsKeywordsPrefNavigation()
        setupSystemSettingsPrefNavigation()
    }

    private fun setupChangeListener() {
        listOf(
            R.string.key_notifications_pref,
            R.string.key_notifications_sync_frequency_in_minutes_pref,
            R.string.key_theme_pref
        ).forEach { keyId ->
            val pref = findPreference<Preference>(getString(keyId))

            pref?.onPreferenceChangeListener = this
        }
    }

    private fun setupNotificationsKeywordsPrefNavigation() {
        val keywordsPrefKey = getString(R.string.key_notifications_keywords_pref)
        val keywordsPref = preferenceScreen.findPreference<Preference>(keywordsPrefKey)

        keywordsPref?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.fragment_notification_keywords)

            true
        }
    }

    private fun setupSystemSettingsPrefNavigation() {
        val systemSettingsPrefKey = getString(R.string.key_notifications_system_pref)
        val systemSettingsPref = preferenceScreen.findPreference<Preference>(systemSettingsPrefKey)
        AppCompatDelegate.MODE_NIGHT_NO
        systemSettingsPref?.setOnPreferenceClickListener {
            navigateToSystemAppNotificationSettings()

            true
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
                syncRecallsWorkerScheduler.scheduleAccordingToPreferences()
                true
            }
            getString(R.string.key_theme_pref) -> {
                if (newValue is String) {
                    requireContext().applyThemePref(newValue)

                    true
                } else {
                    false
                }
            }
            else -> false
        }
    }

    private fun handleNotificationsPreferenceChange(newValue: Any?) {
        if (newValue is String) {
            enableOrDisableNotificationsPreferences(newValue)

            syncRecallsWorkerScheduler.scheduleAccordingToPreferences()
        }
    }

    private fun enableOrDisableNotificationsPreferences(notificationsPrefValue: String) {
        val syncFrequencyPrefKey =
            getString(R.string.key_notifications_sync_frequency_in_minutes_pref)
        val syncFrequencyPref = preferenceScreen.findPreference<Preference>(syncFrequencyPrefKey)
        val systemSettingsPrefKey = getString(R.string.key_notifications_system_pref)
        val systemSettingsPref = preferenceScreen.findPreference<Preference>(systemSettingsPrefKey)
        val noValue = getString(R.string.value_notifications_pref_no)
        syncFrequencyPref?.isEnabled = notificationsPrefValue != noValue
        systemSettingsPref?.isEnabled = notificationsPrefValue != noValue

        val keywordsPrefKey = getString(R.string.key_notifications_keywords_pref)
        val keywordsPref = preferenceScreen.findPreference<Preference>(keywordsPrefKey)
        val keywordValue = getString(R.string.value_notifications_pref_keyword)
        keywordsPref?.isEnabled = notificationsPrefValue == keywordValue
    }
}
