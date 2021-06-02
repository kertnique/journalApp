package com.example.rgr

class Group (var name: String, var students: Array<Student>) {
    fun size(): Int {
        return students.size
    }
    fun pass(points: Int): Int{
        var num = 0
        for(i in students){
            if(i.totalPoints() >= points.toFloat()) num++
        }
        return num
    }

    fun stringify (): String {
        var string = name
        for (i in students){
            string+="|"+i.stringify()
        }
        return string
    }
}