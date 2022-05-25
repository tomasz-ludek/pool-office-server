package com.example.plugins

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

fun dataFromSensor() : String{
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
            if(!response.isSuccessful) throw IOException("Problem - $response")
            for ((name, value) in response.headers) {
                println("$name: $value")
            }
            return response.body!!.string()
    }
}