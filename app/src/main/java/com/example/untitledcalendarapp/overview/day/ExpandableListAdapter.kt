package com.example.untitledcalendarapp.overview.day

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.untitledcalendarapp.R
import com.example.untitledcalendarapp.data.Task

class ExpandableListAdapter internal constructor(private val context: Context, private val titleList: List<String>, private val dataList: LinkedHashMap<String, List<TaskDayListAdapter>>, private val taskList: List<LiveData<List<Task>>>?) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): TaskDayListAdapter {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val childAdapter = getChild(listPosition, expandedListPosition) as TaskDayListAdapter
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.card_recycler_layout, null)
        }
        val childRecyclerView = convertView!!.findViewById<RecyclerView>(R.id.task_expand_grid)
        childRecyclerView.adapter = childAdapter

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.list_title)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle

        val listCountTextView = convertView!!.findViewById<TextView>(R.id.list_contents)
        listCountTextView.setTypeface(null, Typeface.BOLD)
        listCountTextView.text = getChild(listPosition,0).currentList.size.toString()
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    fun setNewItems() {
        notifyDataSetChanged()
    }
}
