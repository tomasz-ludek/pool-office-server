package pl.ludek.poolserver

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.mockito.Mockito.*
import pl.ludek.poolserver.plugins.SimpleModbusRTURelay
import pl.ludek.poolserver.plugins.configureRouting
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
    fun testPoolInfoControllerBedAddress() = testApplication {
        application {
            configureRouting()
        }
        client.get("/pool-info/user").apply {
            assertEquals(HttpStatusCode.NotFound, status)
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
         val smor: SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
         `when`(smor.onRelay(1)).thenReturn("Test")
        smor.onRelay(1)
        verify(smor).onRelay(1)
        assertEquals("Test", smor.onRelay(1))
        verify(smor, atLeast(2)).onRelay(1)
    }

    @Test
    fun testSimpleOffRelay(){
        val smor: SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
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

    @Test
    fun testSimpleModbusRTURelayFalseConnectionOnRelaySpy() = testApplication {
        application {
            configureRouting()
        }
        client.post("/relay/0/1").apply {
            val smor: SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
            `when`(smor.onRelay(1)).thenReturn("Test")
            smor.onRelay(1)
            verify(smor).onRelay(1)
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Test", smor.onRelay(1))
        }
    }

    @Test
    fun testSimpleModbusRTURelayFalseConnectionOffRelaySpy() = testApplication {
        application {
            configureRouting()
        }
        client.post("/relay/0/1").apply {
            val smor: SimpleModbusRTURelay = spy(SimpleModbusRTURelay())
            `when`(smor.offRelay(1)).thenReturn("Test")
            smor.offRelay(1)
            verify(smor).offRelay(1)
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Test", smor.offRelay(1))
        }
    }

    @Test
    fun testSimpleModbusRTURelayFalseConnectionBedAddress() = testApplication {
        application {
            configureRouting()
        }
        client.post("/relay/0/1/1").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}