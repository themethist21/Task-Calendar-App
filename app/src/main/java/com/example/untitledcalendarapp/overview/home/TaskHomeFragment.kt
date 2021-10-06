package com.example.untitledcalendarapp.overview.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Query
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.databinding.FragmentTaskHomeBinding
import com.example.untitledcalendarapp.overview.CalendarApplication
import com.example.untitledcalendarapp.overview.TaskListAdapter

class TaskHomeFragment : Fragment() {
    private val viewModel: HomeViewModel by activityViewModels{
        HomeViewModelFactory(
            (activity?.application as CalendarApplication).database.taskDao()
        )
    }
    private var _binding: FragmentTaskHomeBinding? = null
    private val binding get() = _binding!!

    private var searching:Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Create and bind Adapter
        val adapter = TaskListAdapter{
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToTaskDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.taskHomeGrid.adapter = adapter
        //observe items
        viewModel.allItems.observe(this.viewLifecycleOwner){ items ->
            if(binding.searchView.query.isNullOrBlank()){
                    items.let{
                        adapter.submitList(it)
                    }
                }
        }
        //set layout manager
        binding.taskHomeGrid.layoutManager = GridLayoutManager(this.context,2)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                setQueryTasks(query, adapter)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                setQueryTasks(newText, adapter)
               return true
            }
        })
        val popupMenu = PopupMenu(this.requireContext(),binding.sortButton)
        setUpMenu(popupMenu, adapter)
        binding.sortButton.setOnClickListener { popupMenu.show() }

    }
    private fun setQueryTasks(query:String?,adapter: TaskListAdapter){
        query?.let{
            if (it.isNotBlank()){
                searching = true
                viewModel.getTasksByQuery(it)?.observe(this.viewLifecycleOwner){items ->
                    items.let{
                        adapter.submitList(it)
                    }
                }
            } else {
                searching = false
                viewModel.allItems.value.let{
                    adapter.submitList(it)
                }
            }
        }
    }
    private fun setUpMenu(menu:PopupMenu,adapter: TaskListAdapter){
        menu.menu.add(Menu.NONE,0,0,"Sort by...")
        menu.menu.add(Menu.NONE,1,1,"Title ASC")
        menu.menu.add(Menu.NONE,2,2,"Title DESC")
        menu.menu.add(Menu.NONE,3,3,"Date ASC")
        menu.menu.add(Menu.NONE,4,4,"Date DESC")
        menu.menu.add(Menu.NONE,5,5,"Most important")
        menu.menu.add(Menu.NONE,6,6,"Least important")
        menu.menu.getItem(0).isEnabled = false

        menu.setOnMenuItemClickListener{
            viewModel.getOrderedTasks(it.itemId)
            viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
                if (searching == false) {
                    items.let {
                        adapter.submitList(it)
                    }
                }
            }
            false
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