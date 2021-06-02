package com.example.rgr


class Mark (var date: String, var value: Double) {
    fun stringify(): String{
        return "${date}&${value}"
    }
}