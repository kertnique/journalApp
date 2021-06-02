package com.example.rgr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.rgr.R.id.*

class StudentAdapter(val context: Context, var dataSource: Array<Student>): BaseAdapter ( ) {
    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.students_layout, parent, false)
        val titleTextView = rowView.findViewById(students_layout_title) as TextView
        val subtitleTextView = rowView.findViewById(students_layout_subtitle) as TextView
        val detailTextView = rowView.findViewById(students_layout_detail) as TextView
        val student = getItem(position) as Student
        titleTextView.text = student.name + " " + student.surname
        subtitleTextView.text  = "Перша атестація - " + student.pass(att1) + ". Друга атестація - " + student.pass(att2) + ".\nДопуск до екзамену - " + student.pass(dopusk) + "."
        detailTextView.text = student.totalPoints().toString()
        return rowView
    }

    override fun getItem(position: Int): Student {
        return  dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    fun clearData(){
        dataSource = emptyArray<Student>()
    }
}