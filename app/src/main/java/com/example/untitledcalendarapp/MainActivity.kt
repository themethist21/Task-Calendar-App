package com.example.untitledcalendarapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.untitledcalendarapp.databinding.ActivityMainBinding
import com.example.untitledcalendarapp.overview.home.ViewPagerFragmentDirections
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    private fun setupNavigation() {
        // find the nav controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar)
        // setup the action bar with drawer layout
        //setupActionBarWithNavController(navController, binding.drawerLayout)

        NavigationUI.setupActionBarWithNavController(this,navController, binding.drawerLayout)

        NavigationUI.setupWithNavController(binding.navigationView, navController)

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)


        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            val toolBar = supportActionBar ?: return@addOnDestinationChangedListener
            when(destination.id) {
                R.id.home -> {
                    toolBar.setDisplayShowTitleEnabled(false)
                }
                else -> {
                    toolBar.setDisplayShowTitleEnabled(true)
                }
            }
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                val action = ViewPagerFragmentDirections.actionViewPagerFragmentToNavSettings()
                navController.navigate(action)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat?,
        pref: Preference?
    ): Boolean {
        if (pref!!.key.equals("notification_settings")) {
            navController.navigate(R.id.notificationSettingsFragment)
        } else if (pref!!.key.equals("datetime_settings")){
            navController.navigate(R.id.timedaySettingsFragment)
        }
        return true
    }
}