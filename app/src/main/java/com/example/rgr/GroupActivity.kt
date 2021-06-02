package com.example.rgr

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.File



class GroupActivity : AppCompatActivity() {

    companion object {
        var position1 = ""

        fun newIntent(context: Context, posit : String): Intent {
            val detailIntent = Intent(context, GroupActivity::class.java)
            detailIntent.putExtra(position1, posit)
            return detailIntent
        }
    }

    private lateinit var listView: ListView
    private lateinit var group: Group
    private lateinit var position: String

     override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
         position = intent.getStringExtra(position1).toString()
         group = groupDestringify(position.toInt())
         title = group.name
        listView = findViewById(R.id.listView)
        val adapter = StudentAdapter(this, group.students)
        listView.adapter = adapter
         listView.setOnItemClickListener { _, _, positions, _ ->
             val detailIntent = MarksActivity.newIntent(this, position, positions.toString())
             startActivity(detailIntent)
         }
    }

    override fun onResume() {
        super.onResume()
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        val isFile = file.createNewFile()
        if(!isFile){
            groupDestringify(position.toInt())
        }
        listView = findViewById(R.id.listView)
        drawListView()
    }

    fun addStudent(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Введіть ім'я та прізвище студента")
        val dialogLayout = inflater.inflate(R.layout.dialogaddstudent, null)
        val editName = dialogLayout.findViewById<EditText>(R.id.editText)
        val editSurname = dialogLayout.findViewById<EditText>(R.id.editText2)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
            addnewStudent(editName.text.toString(),editSurname.text.toString())
            drawListView()
            groupStringify(position.toInt())
        }
        builder.show()

    }

    fun drawListView(){
        var adapter = StudentAdapter(this, group.students)
        adapter.clearData()
        adapter = StudentAdapter(this, group.students)
        adapter.notifyDataSetChanged()
        listView.adapter = adapter
        listView.invalidate()
    }

    fun addnewStudent(surname: String, name: String){
        if(name != "" && surname != ""){
            var isAlready = 0
            for(i in group.students){
                if(name == i.name && surname == i.surname) isAlready = 1
            }
            if(isAlready == 0) group.students += Student(name, surname, emptyArray<Mark>())
        }
    }

    fun groupDestringify (position: Int): Group{
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        var groups = file.readText().split("\n")
        var array = groups[position + 3].split("|")
        var i  = 1
        var students = emptyArray<Student>()
        while (i < array.size){
            var student = array[i].split("=")
            var marks = emptyArray<Mark>()
            var j = 2
            while(j < student.size){
                var mark = student[j].split("&")
                marks += Mark(mark[0] , mark[1].toDouble())
                j++
            }
            students += Student(student[0],student[1],marks)
            i++
        }
        return Group(array[0], students)
    }

    fun groupStringify (position: Int){
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        var groups = file.readText().split("\n").toMutableList()
        groups[position+3] = group.stringify()
        val string = groups.joinToString(separator = "\n") { it -> it }
        file.delete()
        file.createNewFile()
        file.writeText(string)
    }

    fun groupDelete (position: Int){
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        var groups = file.readText().split("\n").toMutableList()
        var new = groups.filterIndexed { index, s -> index != position + 3  }
        val string = new.joinToString(separator = "\n") { it -> it }
        file.delete()
        file.createNewFile()
        file.writeText(string)
    }

    fun goBack (view: View) {
        groupStringify(position.toInt())
        val backIntent = MainActivity.newIntent(this)
        startActivity(backIntent)
    }

    fun deleteGroup (view: View){
        groupDelete(position.toInt())
        val backIntent = MainActivity.newIntent(this)
        startActivity(backIntent)
    }

}