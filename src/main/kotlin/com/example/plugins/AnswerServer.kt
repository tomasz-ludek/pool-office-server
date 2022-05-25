package com.example.plugins
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun answerServer(): String? {
    val mapper = jacksonObjectMapper()
    val t1 = "889_"
    val t2 = "890_"
    val t3 = "891_"
    val p1 = "975_"
    val data: String = dataFromSensor()
    val dataInit:JsonData =
        JsonData(dataFromString(data,t1).toFloat(),
                 dataFromString(data,t2).toFloat(),
                 dataFromString(data,t3).toFloat(),
                  dataFromString(data,p1).toFloat()
    )
    val jsonObj =mapper.writeValueAsString(dataInit)
    return jsonObj
}

