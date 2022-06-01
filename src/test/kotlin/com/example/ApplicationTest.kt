package com.example

import com.example.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.mockito.Mockito.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testPoolInfoController() = testApplication {
        application {
            configureRouting()
        }
        client.get("/pool-info").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(40, bodyAsText().length)
        }
    }
    @Test
    fun testSimpleModbusRTURelayFalseConnectionOnRelay() = testApplication {
        application {
            configureRouting()
        }
        client.post("/relay/0/1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("No have port", SimpleModbusRTURelay().onRelay(1))
        }
    }
    @Test
    fun testSimpleModbusRTURelayFalseConnectionOffRelay() = testApplication {
        application {
            configureRouting()
        }
        client.post("/relay/0/1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("No have port", SimpleModbusRTURelay().offRelay(1))
        }
    }


    @Test
    fun testSimpleOnRelay(){
         val smor:SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
         `when`(smor.onRelay(1)).thenReturn("Test")
        smor.onRelay(1)
        verify(smor).onRelay(1)
        assertEquals("Test", smor.onRelay(1))
        verify(smor, atLeast(2)).onRelay(1)
    }

    @Test
    fun testSimpleOffRelay(){
        val smor:SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
        `when`(smor.offRelay(1)).thenReturn("Test")
        smor.offRelay(1)
        verify(smor).offRelay(1)
        assertEquals("Test", smor.offRelay(1))
        verify(smor, atLeast(2)).offRelay(1)
    }

    // To pass the test, you need to make the function "connectionToPort" public.
//    @org.junit.Test
//    fun testSimpleConnectRelayOn(){
//        val smor:SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
//        `when`(smor.connectionToPort(1,1)).thenReturn("Test")
//        smor.connectionToPort(1,1)
//        verify(smor).connectionToPort(1,1)
//       // assertEquals("Test", smor.connectionToPort(1,1))
//        assertEquals("Test", smor.onRelay(1))
//        //verify(smor, atLeast(2)).offRelay(1)
//    }

    // To pass the test, you need to make the function "connectionToPort" public.
//    @org.junit.Test
//    fun testSimpleConnectRelayOff(){
//        val smor:SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
//        `when`(smor.connectionToPort(1,0)).thenReturn("Test")
//        smor.connectionToPort(1,0)
//        verify(smor).connectionToPort(1,0)
//        // assertEquals("Test", smor.connectionToPort(1,0))
//        assertEquals("Test", smor.offRelay(1))
//        //verify(smor, atLeast(2)).offRelay(1)
//    }

}