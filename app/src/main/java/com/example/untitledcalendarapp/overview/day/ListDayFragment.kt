package com.example.untitledcalendarapp.overview.day

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.databinding.FragmentListDayBinding
import com.example.untitledcalendarapp.databinding.FragmentTaskHomeBinding
import com.example.untitledcalendarapp.overview.CalendarApplication
import com.example.untitledcalendarapp.overview.TaskListAdapter
import com.example.untitledcalendarapp.overview.home.HomeViewModel
import com.example.untitledcalendarapp.overview.home.HomeViewModelFactory
import com.example.untitledcalendarapp.overview.home.ViewPagerFragmentDirections

class ListDayFragment : Fragment() {

    private val viewModel: DayViewModel by activityViewModels{
        DayViewModelFactory(
            (activity?.application as CalendarApplication).database.taskDao()
        )
    }

    private var _binding: FragmentListDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListDayBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TaskDayListAdapter{
            val action = SpecificDayFragmentDirections.actionSpecificDayFragmentToTaskDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.taskDayGrid.adapter = adapter
        //observe items
        viewModel.allDayItems.observe(this.viewLifecycleOwner){ items ->
            items.let{
                adapter.submitList(it)
            }
        }
        //set layout manager
        binding.taskDayGrid.layoutManager = GridLayoutManager(this.context,2)
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