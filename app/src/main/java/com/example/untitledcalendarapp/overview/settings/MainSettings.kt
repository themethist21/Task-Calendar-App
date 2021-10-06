package com.example.untitledcalendarapp.overview.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.untitledcalendarapp.R

class MainSettings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}