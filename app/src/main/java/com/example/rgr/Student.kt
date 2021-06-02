package com.example.rgr

import kotlin.math.roundToInt

class Student (var name: String, var surname: String, var marks: Array<Mark>) {
        fun totalPoints(): Double {
            var total = 0.toDouble()
            for(i in marks) total += i.value
            return (total * 10).roundToInt().toDouble() / 10
        }

    fun pass(points: Int): String{
        return if (totalPoints() > points) "Є"
        else "Немає"
    }

    fun stringify(): String{
        var string = "$name=$surname"
        for(i in marks){
            string += "="+i.stringify()
        }
        return string
    }
}