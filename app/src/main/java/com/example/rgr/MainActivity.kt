package com.example.rgr

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

var groups = emptyArray<Group>()
private lateinit var listView: ListView
var att1 = 10
var att2 = 30
var dopusk = 50


class MainActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        val adapter = GroupAdapter(this, groups)
        listView.adapter = adapter
        drawListView()
        changeText()
        val context = this
        listView.setOnItemClickListener { _, _, position, _ ->
            val detailIntent = GroupActivity.newIntent(context, position.toString())
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
            groupsReadFile()
        }
        listView = findViewById(R.id.listView)
        drawListView()
    }

    fun AddGroup(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Введіть назву групи")
        val dialogLayout = inflater.inflate(R.layout.dialogaddgroup, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                addNewGroup(editText.text.toString())
                drawListView()
                groupsWriteFile()
        }
        builder.show()

    }

    fun changeParams(view: View){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Змініть параметри")
        val dialogLayout = inflater.inflate(R.layout.dialogchangeparams, null)
        val num1 = dialogLayout.findViewById<EditText>(R.id.editTextNumber)
        val num2 = dialogLayout.findViewById<EditText>(R.id.editTextNumber2)
        val num3 = dialogLayout.findViewById<EditText>(R.id.editTextNumber3)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK"){ dialogInterface: DialogInterface, i: Int ->
            if(num1.text.toString() != "")att1 = num1.text.toString().toInt()
            if(num2.text.toString() != "")att2 = num2.text.toString().toInt()
            if(num3.text.toString() != "")dopusk = num3.text.toString().toInt()
            changeText()
            groupsWriteFile()
        }
        builder.setNegativeButton("Скасувати"){ dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
    }

    private fun drawListView(){
        var adapter = GroupAdapter(this, groups)
        adapter.clearData()
        adapter = GroupAdapter(this, groups)
        adapter.notifyDataSetChanged()
        listView.adapter = adapter
        listView.invalidate()
    }

    private fun addNewGroup(text: String){
        if(text != ""){
            var isAlready = 0
            for(i in groups){
                if(text == i.name) isAlready = 1
            }
            if(isAlready == 0) groups += Group(text, emptyArray<Student>())
        }
    }

    private fun changeText(){
        val textView: TextView = findViewById<TextView>(R.id.textView)
        textView.text = "  I атестація - ${att1} балів.\n  ІІ атестація - ${att2} балів.\n  Допуск - ${dopusk} балів."
    }

    fun groupsReadFile(){
        groups = emptyArray()
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        val _groups = file.readText().split("\n")
            att1 = _groups[0].toInt()
            att2 = _groups[1].toInt()
            dopusk = _groups[2].toInt()
            var it = 3
            while (it < _groups.size) {
                var array = _groups[it].split("|")
                var i = 1
                var students = emptyArray<Student>()
                while (i < array.size) {
                    var student = array[i].split("=")
                    var marks = emptyArray<Mark>()
                    var j = 2
                    while (j < student.size) {
                        var mark = student[j].split("&")
                        marks += Mark(mark[0], mark[1].toDouble())
                        j++
                    }
                    students += Student(student[0], student[1], marks)
                    i++
                }
                groups += Group(array[0], students)
                it++
        }
    }

    fun groupsWriteFile(){
        var string = "${att1}\n${att2}\n${dopusk}\n"
        for(i in groups){
            string += i.stringify() + "\n"
        }
        string = string.dropLast(1)
        val context = this
        val path = context.applicationInfo.dataDir
        val file = File("$path/data.txt")
        file.delete()
        file.createNewFile()
        file.writeText(string)
    }

}
