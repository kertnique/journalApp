package com.example.rgr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.rgr.R.id.marks_layout_detail
import com.example.rgr.R.id.marks_layout_title
import kotlin.math.roundToInt

class MarkAdapter(val context: Context, var dataSource: Array<Mark>): BaseAdapter ( ) {
    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.marks_layout, parent, false)
        val titleTextView = rowView.findViewById(marks_layout_title) as TextView
        val detailTextView = rowView.findViewById(marks_layout_detail) as TextView
        val mark = getItem(position) as Mark
        titleTextView.text = mark.date
        detailTextView.text = ((mark.value * 10).roundToInt() / 10).toString() // round to 1 decimal
        return rowView
    }

    override fun getItem(position: Int): Mark {
        return  dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    fun clearData(){
        dataSource = emptyArray<Mark>()
    }

}