package com.example.rgr

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MarksActivity : AppCompatActivity() {

    companion object {
        var positiongroup = ""
        var positionstudent = "0"
        fun newIntent(context: Context, posit : String, posit2: String): Intent {
            val detailIntent = Intent(context, MarksActivity::class.java)
            detailIntent.putExtra(positiongroup, posit)
            detailIntent.putExtra(positionstudent, posit2)
            return detailIntent
        }
    }

    lateinit var positionGroup: String
    lateinit var positionStudent: String
    lateinit var student: Student
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marks)
        positionGroup = intent.getStringExtra(positiongroup).toString()
        positionStudent = intent.getStringExtra(positionstudent).toString()
        student = studentDestringify(positionGroup.toInt(),positionStudent.toInt())
        title = student.name + " " + student.surname
        listView = findViewById(R.id.listView)
        val adapter = MarkAdapter(this, student.marks)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            markDialog(position)
        }
    }

    fun studentDestringify(group: Int, student: Int): Student{
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        var groups = file.readText().split("\n")
        var students = groups[group + 3].split('|')
        var ourStudent = students[student + 1].split('=')
        var marks = emptyArray<Mark>()
        var j = 2
        while(j < ourStudent.size){
            var mark = ourStudent[j].split('&')
            marks += Mark(mark[0] , mark[1].toDouble())
            j++
        }
        return Student(ourStudent[0],ourStudent[1],marks)
    }

    fun addMark (view: View){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Введіть дату та значення оцінки")
        val dialogLayout = inflater.inflate(R.layout.dialogaddmark, null)
        builder.setView(dialogLayout)
        val editValue = dialogLayout.findViewById<EditText>(R.id.editTextNumberDecimals).text
        val calendar = dialogLayout.findViewById<CalendarView>(R.id.calendarView)
        var calendarView = calendar.date
        calendar.setOnDateChangeListener { view, year, month, day ->
            val c = Calendar.getInstance()
            c[year, month] = day
            calendarView = c.timeInMillis
        }
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
            if(editValue.toString() != "") {
                var sdf = SimpleDateFormat("dd/MM/yyyy")
                student.marks += Mark(sdf.format(Date(calendarView)), editValue.toString().toDouble())
                studentStringify(positionGroup.toInt(), positionStudent.toInt())
                drawListView()
            }
            else Toast.makeText(this, "Wrong input", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    fun studentStringify (group: Int, numStudent: Int){
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        var groups = file.readText().split("\n").toMutableList()
        var students = groups[group+3].split("|").toMutableList()
        students[numStudent+1] = student.stringify()
        groups[group + 3] = students.joinToString(separator = "|") { it -> it }
        val string = groups.joinToString(separator = "\n") { it -> it }
        file.delete()
        file.createNewFile()
        file.writeText(string)
    }

    private fun drawListView(){
        var adapter = MarkAdapter(this, student.marks)
        adapter.clearData()
        adapter = MarkAdapter(this, student.marks)
        adapter.notifyDataSetChanged()
        listView.adapter = adapter
        listView.invalidate()

    }

    fun studentDelete(group: Int, numStudent: Int){
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        var groups = file.readText().split("\n").toMutableList()
        var students = groups[group + 3].split("|").toMutableList()
        var new = students.filterIndexed{i,string -> i != numStudent + 1}
        groups[group + 3] = new.joinToString(separator = "|") {it -> it}
        val string = groups.joinToString(separator = "\n") { it -> it }
        file.delete()
        file.createNewFile()
        file.writeText(string)
    }

    fun markDelete(group: Int, numStudent: Int, position: Int){
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        var groups = file.readText().split("\n").toMutableList()
        var students = groups[group + 3].split("|").toMutableList()
        var marks = students[numStudent + 1].split("=").toMutableList()
        var new = marks.filterIndexed{index,string -> index != position + 2}
        students[numStudent + 1] = new.joinToString(separator = "=") { it -> it }
        groups[group + 3] = students.joinToString(separator = "|") {it -> it}
        val string = groups.joinToString(separator = "\n") { it -> it }
        file.delete()
        file.createNewFile()
        file.writeText(string)
        student = studentDestringify(group, numStudent)
        drawListView()
    }

    fun deleteStudent(view: View){
        studentDelete(positionGroup.toInt(),positionStudent.toInt())
        val backIntent = GroupActivity.newIntent(this, positionGroup)
        startActivity(backIntent)
    }

    fun goBack(view: View){
        studentStringify(positionGroup.toInt(),positionStudent.toInt())
        val backIntent = GroupActivity.newIntent(this, positionGroup)
        startActivity(backIntent)
    }

    fun markDialog(position: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Видалити оцінку?")
        builder.setPositiveButton("OK"){dialogInterface: DialogInterface, i: Int ->
            markDelete(positionGroup.toInt(),positionStudent.toInt(),position)
        }
        builder.setNegativeButton("Скасувати"){dialog: DialogInterface?, i: Int ->  }
        builder.show()
    }
}