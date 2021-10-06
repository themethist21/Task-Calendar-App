package com.example.untitledcalendarapp.overview

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.data.Task
import com.example.untitledcalendarapp.databinding.CardViewLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskListAdapter(private val onItemClicked: (Task) -> Unit):
    ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        return TaskViewHolder(
            CardViewLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class TaskViewHolder(private var binding:CardViewLayoutBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(task: Task){
            binding.apply {
                val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
                taskTitle.text = task.taskTitle.toString()
                taskDescription.text = task.taskDescription.toString()
                taskDate.text = sdf.format(task.taskDate?.time)
                when(task.taskPrio){
                    0 -> taskPriority.setImageResource(R.color.green)
                    1 -> taskPriority.setImageResource(R.color.yellow)
                    2 -> taskPriority.setImageResource(R.color.red)
                }
            }
        }
    }
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.taskTitle == newItem.taskTitle
            }
        }
    }
}

