package com.example.rgr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.rgr.R.id.*

class GroupAdapter(val context: Context, var dataSource: Array<Group>): BaseAdapter ( ) {
    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.groups_layout, parent, false)
        val titleTextView = rowView.findViewById(groups_layout_title) as TextView
        val subtitleTextView = rowView.findViewById(groups_layout_subtitle) as TextView
        val detailTextView = rowView.findViewById(groups_layout_detail) as TextView
        val group = getItem(position) as Group
        titleTextView.text = group.name
        subtitleTextView.text  = "Перша атестація - " + group.pass(att1).toString() + ". Друга атестація - " + group.pass(att2).toString() + ".\nДопуск до екзамену - " + group.pass(dopusk).toString() + "."
        detailTextView.text = group.size().toString()
        return rowView
    }

    override fun getItem(position: Int): Group {
        return  dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    fun clearData(){
        dataSource = emptyArray<Group>()
    }

}