package com.example.untitledcalendarapp.overview.create

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.data.Task
import com.example.untitledcalendarapp.databinding.FragmentTaskCreateBinding
import com.example.untitledcalendarapp.overview.CalendarApplication
import com.example.untitledcalendarapp.overview.home.HomeViewModel
import com.example.untitledcalendarapp.overview.home.HomeViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TaskCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskCreateFragment : Fragment() {
    private val dataViewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(
            (activity?.application as CalendarApplication).database.taskDao()
        )
    }
    private val viewModel:CreateViewModel by activityViewModels()

    lateinit var task: Task

    private val navigationArgs: TaskCreateFragmentArgs by navArgs()

    private var _binding: FragmentTaskCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskCreateBinding.inflate(inflater, container, false)

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.taskId
        initialBinding()

        if (id > 0){
            dataViewModel.retrieveTask(id).observe(this.viewLifecycleOwner) {selectedTask ->
                task = selectedTask
                bind(task)
            }
        } else {
            viewModel.resetCalendar()
            binding.saveAction.setOnClickListener { addNewItem() }
            binding.prioritySpinner.setSelection(0)
        }

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_close)
    }

    private fun initialBinding(){
        val adapter =  ArrayAdapter.createFromResource(requireActivity(),R.array.Priorities,R.layout.spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.apply{

            dateButton.setOnClickListener { showDatePickerDialog() }
            timeButton.setOnClickListener { showTimePickerDialog() }
            prioritySpinner.adapter = adapter
        }
        viewModel.day.observe(viewLifecycleOwner,
            {   val sdf = SimpleDateFormat("MMM dd",Locale.getDefault())
                binding.dateText.text =sdf.format(viewModel.calendar.time)
            })
        viewModel.hourOfDay.observe(viewLifecycleOwner,
            {   val sdf = SimpleDateFormat("HH:mm",Locale.getDefault())
                binding.timeText.text =sdf.format(viewModel.calendar.time)
            })
    }

    private fun bind(task: Task){
        val taskCalendar = task.taskDate
        binding.apply{
            taskTitleInput.setText(task.taskTitle, TextView.BufferType.SPANNABLE)
            taskDescriptionInput.setText(task.taskDescription, TextView.BufferType.SPANNABLE)
            prioritySpinner.setSelection(task.taskPrio)
            viewModel.overrideCalendar(taskCalendar)
            saveAction.setOnClickListener { updateTask() }
        }
    }

    private fun isEntryValid(): Boolean {
        return dataViewModel.isEntryValid(
            binding.taskTitleInput.text.toString(),
            binding.taskDescriptionInput.text.toString()
        )
    }
    private fun addNewItem() {
        if (isEntryValid()) {
            dataViewModel.addNewTask(
                binding.taskTitleInput.text.toString(),
                binding.taskDescriptionInput.text.toString(),
                viewModel.calendar,
                binding.prioritySpinner.selectedItemPosition
            )
            findNavController().popBackStack()

        }
    }

    private fun updateTask() {
        if (isEntryValid()) {
            dataViewModel.updateTask(
                this.navigationArgs.taskId,
                this.binding.taskTitleInput.text.toString(),
                this.binding.taskDescriptionInput.text.toString(),
                this.viewModel.calendar,
                this.binding.prioritySpinner.selectedItemPosition
            )
            findNavController().navigateUp()
        }
    }
    private fun showDatePickerDialog() {
        DateDialogFragment().show(this.parentFragmentManager, "datePicker")
    }
    private fun showTimePickerDialog() {
        TimeDialogFragment().show(this.parentFragmentManager, "timePicker")
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

