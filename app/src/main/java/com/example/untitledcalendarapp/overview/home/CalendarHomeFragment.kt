package com.example.untitledcalendarapp.overview.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.databinding.FragmentCalendarHomeBinding
import com.example.untitledcalendarapp.overview.CalendarApplication
import com.example.untitledcalendarapp.overview.home.HomeViewModel
import com.example.untitledcalendarapp.overview.home.HomeViewModelFactory
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [CalendarHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarHomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(
            (activity?.application as CalendarApplication).database.taskDao()
        )
    }
    private var _binding: FragmentCalendarHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarHomeBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentCalendar: Calendar = Calendar.getInstance()
        binding.apply {
            val sdf = SimpleDateFormat("MMM dd",Locale.getDefault())
            calendar.setDate(currentCalendar)
            fab.text = sdf.format(currentCalendar.time)

            fab.setOnClickListener{
                calendar.showCurrentMonthPage()
            }
            calendar.setOnDayClickListener(OnDayClickListener { eventDay ->
                val date = eventDay.calendar.time.toString()

                val action = ViewPagerFragmentDirections
                    .actionViewPagerFragmentToSpecificDayFragment(date = date,
                        title = sdf.format(eventDay.calendar.time))
                Navigation.findNavController(view).navigate(action)
            })
        }
        viewModel.allDays.observeForever{} 
        viewModel.allTasks.observe(this.viewLifecycleOwner){
            viewModel.getDailyTasks()
            val events = mutableListOf<EventDay>()
            for(day in viewModel.tasksByDay){
                day.observe(this.viewLifecycleOwner){ tasks ->
                    tasks.let{
                        when (it.size){
                            0 -> {}
                            1 -> events.add(EventDay(it[0].taskDate, R.drawable.ic_task))
                            2 -> events.add(EventDay(it[0].taskDate, R.drawable.ic_two_tasks))
                            3 -> events.add(EventDay(it[0].taskDate, R.drawable.ic_three_tasks))
                            else ->{
                                events.add(EventDay(it[0].taskDate, R.drawable.ic_four_tasks))
                            }
                        }
                    }
                    binding.calendar.setEvents(events)
                    binding.calendar.setDate(currentCalendar)
                }
            }
        }


        super.onViewCreated(view, savedInstanceState)
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