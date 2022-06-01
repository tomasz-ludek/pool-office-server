package com.example

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.example.plugins.*
import org.mockito.Mock

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/pool-info").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(40, bodyAsText().length)
        }
    }
    @Test
    fun testSimpleModbusRTURelay(){

    }
}