package pl.ludek.poolserver.plugins

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class PoolInfoController {
@kotlinx.serialization.Serializable
     data class PoolInfoData (val t1: Float, val t2:Float, val t3: Float, val p1: Float)

        private fun dataFromSensor(): String? {
        val client = OkHttpClient.Builder().build()
        val urlBase = "http://192.168.2.169/user/cgi-bin/edition.cgi"
        val username = "ludex"
        val password = "trickypass"
        val postString = "gt=889,890,664,500,893,974a,891,892,892b,976,978,977,975a,975b,899,903,975,974,"

        val request = Request.Builder()
            .url(urlBase)
            .addHeader("Authorization", Credentials.basic(username, password))
            .post(postString.toRequestBody())
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Problem - $response")
            for ((name, value) in response.headers) {
                println("$name: $value")
            }
            return response.body!!.string()
        }
    }

    private fun dataFromString(inputStr :String, sensorNumber: String): String{
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

    fun  answerServer(): PoolInfoData {
        val mapper = jacksonObjectMapper()
        val t1 = "889_"
        val t2 = "890_"
        val t3 = "891_"
        val p1 = "975_"
        val data: String = dataFromSensor().toString()
        val dataInit: PoolInfoData =
            PoolInfoData(
                dataFromString(data, t1).toFloat(),
                dataFromString(data, t2).toFloat(),
                dataFromString(data, t3).toFloat(),
                dataFromString(data, p1).toFloat()
            )
       // val jsonObj =mapper.writeValueAsString(dataInit)
        return dataInit
    }
}