package com.example.untitledcalendarapp.overview.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.untitledcalendarapp.R

class NotificationSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_preferences, rootKey)
    }
}