package com.example.untitledcalendarapp.overview.day

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.data.Task
import com.example.untitledcalendarapp.databinding.FragmentListDayBinding
import com.example.untitledcalendarapp.databinding.FragmentTimeDayBinding
import com.example.untitledcalendarapp.overview.CalendarApplication
import java.text.DecimalFormat
import java.text.NumberFormat

class TimeDayFragment : Fragment() {
    private val viewModel: DayViewModel by activityViewModels{
        DayViewModelFactory(
            (activity?.application as CalendarApplication).database.taskDao()
        )
    }

    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String> ? = null

    private var _binding: FragmentTimeDayBinding? = null
    private val binding get() = _binding!!

    val data: LinkedHashMap<String, List<TaskDayListAdapter>>
        get() {
            val listData = LinkedHashMap<String, List<TaskDayListAdapter>>()
            for (i in 0..23){
                val list = ArrayList<TaskDayListAdapter>()
                val adapter = TaskDayListAdapter{
                    val action = SpecificDayFragmentDirections.actionSpecificDayFragmentToTaskDetailFragment(it.id)
                    this.findNavController().navigate(action)
                }
                adapter.submitList(viewModel.tasksByHour[i].value)

                list.add(adapter)
                listData[i.toString()] = list
            }

            return listData
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimeDayBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listData = data

        titleList = ArrayList(listData.keys)
        adapter = ExpandableListAdapter(this.requireContext(), titleList as ArrayList<String>, listData,viewModel.tasksByHour)
        binding.expandableListView.setAdapter(adapter)
        val baseAdapter = adapter

        for (i in 0..23){
            viewModel.tasksByHour[i].observe(this.viewLifecycleOwner){ items ->
                items.let{
                    val list = listData[i.toString()]
                    if (list != null){
                        list[0].submitList(it)
                    }
                    baseAdapter?.setNewItems()
                }
            }
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
