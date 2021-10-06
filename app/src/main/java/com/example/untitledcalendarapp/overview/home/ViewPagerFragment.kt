package com.example.untitledcalendarapp.overview.home

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.databinding.FragmentCalendarHomeBinding
import com.example.untitledcalendarapp.databinding.FragmentViewPagerBinding
import com.example.untitledcalendarapp.overview.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val NUM_PAGES = 2

class ViewPagerFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var navMenu: BottomNavigationView
    private lateinit var fabButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPagerBinding.inflate(inflater)
        viewPager = binding.pager
        navMenu = binding.bottomNavigationMenu
        fabButton = binding.fabCreate

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = ScreenSlidePagerAdapter(this)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {navMenu.setSelectedItemId(R.id.navigation_calendar)}
                    1 -> {navMenu.setSelectedItemId(R.id.navigation_archive)}
                }
            }
        })
        navMenu.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.navigation_calendar -> {
                            viewPager.currentItem = 0
                            // put your code here
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navigation_archive -> {
                            viewPager.currentItem = 1
                            // put your code here
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                })
        fabButton.setOnClickListener{
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToTaskCreateFragment(
                date = 0, title = getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: ViewPagerFragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return CalendarHomeFragment()
                1 -> return TaskHomeFragment()
            }
            return Fragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}


