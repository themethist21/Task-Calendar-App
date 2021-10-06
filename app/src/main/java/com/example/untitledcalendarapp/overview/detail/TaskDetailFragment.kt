package com.example.untitledcalendarapp.overview.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.data.Task
import com.example.untitledcalendarapp.databinding.FragmentTaskDetailBinding
import com.example.untitledcalendarapp.overview.CalendarApplication
import com.example.untitledcalendarapp.overview.home.HomeViewModel
import com.example.untitledcalendarapp.overview.home.HomeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class TaskDetailFragment : Fragment() {
    private val viewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(
            (activity?.application as CalendarApplication).database.taskDao()
        )
    }
    private val navigationArgs:TaskDetailFragmentArgs by navArgs()

    lateinit var task:Task

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.taskId
        viewModel.retrieveTask(id).observe(this.viewLifecycleOwner){ selectedTask ->
            task = selectedTask
            bind(task)
        }
    }

    private fun bind(task:Task){
        val dateSdf = SimpleDateFormat("MMM dd", Locale.getDefault())
        val timeSdf = SimpleDateFormat("HH:mm",Locale.getDefault())
        binding.apply {
            currentTaskTitle.text = task.taskTitle
            currentTaskDescription.text = task.taskDescription
            currentTaskDate.text = dateSdf.format(task.taskDate?.time)
            currentTaskTime.text = timeSdf.format(task.taskDate?.time)
            when(task.taskPrio){
                0 -> taskPriority.setImageResource(R.color.green)
                1 -> taskPriority.setImageResource(R.color.yellow)
                3 -> taskPriority.setImageResource(R.color.red)
            }
            deleteTask.setOnClickListener { showConfirmationDialog() }
            editTask.setOnClickListener { editItem() }
        }
    }
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }
    private fun deleteItem() {
        viewModel.deleteTask(task)
        findNavController().navigateUp()}

    private fun editItem(){
        val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToTaskCreateFragment(
            title = getString(R.string.edit_fragment_title),
            taskId = task.id
        )
        this.findNavController().navigate(action)
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