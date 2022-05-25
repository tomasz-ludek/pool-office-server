package com.example.plugins

fun dataFromString(inputStr :String, sensorNumber: String) :String {
    val numberStartSubstring = inputStr.indexOf(sensorNumber)
    val subString = inputStr.substring(numberStartSubstring)
    val number1 = subString.indexOf("_")
    val number2 = subString.indexOf(",")
    var rez = ""
    for (i in number1 + 1 until number2) {
        if(i == number2-1){
            rez += "."+ subString[i]
        }else{rez += subString[i]}

    }
     return rez
}