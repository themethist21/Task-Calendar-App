package com.example.untitledcalendarapp.overview.day

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.databinding.FragmentCalendarHomeBinding
import com.example.untitledcalendarapp.databinding.FragmentSpecificDayBinding
import com.example.untitledcalendarapp.overview.CalendarApplication
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private const val NUM_PAGES = 2
class SpecificDayFragment : Fragment() {

    private val viewModel: DayViewModel by activityViewModels{
        DayViewModelFactory(
            (activity?.application as CalendarApplication).database.taskDao()
        )
    }

    private var _binding: FragmentSpecificDayBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: SpecificDayFragmentArgs by navArgs()

    private lateinit var date:String
    private lateinit var viewPager: ViewPager2
    private lateinit var navMenu: BottomNavigationView
    private lateinit var fabButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let{
            date = it.getString("date").toString()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentSpecificDayBinding.inflate(inflater)
        viewPager = binding.dayPager
        navMenu = binding.bottomNavigationDay
        fabButton = binding.fabButtonCreate

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setAllTasks(navigationArgs.title)

        viewPager.adapter = ScreenSlidePagerAdapter(this)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {navMenu.setSelectedItemId(R.id.navigation_list)}
                    1 -> {navMenu.setSelectedItemId(R.id.navigation_time)}
                }
            }
        })
        navMenu.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.navigation_list -> {
                            viewPager.currentItem = 0
                            // put your code here
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.navigation_time -> {
                            viewPager.currentItem = 1
                            // put your code here
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                })
        fabButton.setOnClickListener{
            val action = SpecificDayFragmentDirections
                .actionSpecificDayFragmentToTaskCreateFragment(date = 0,
                    title = getString(R.string.add_fragment_title))
            Navigation.findNavController(view).navigate(action)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private inner class ScreenSlidePagerAdapter(fa: SpecificDayFragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return ListDayFragment()
                1 -> return TimeDayFragment()
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