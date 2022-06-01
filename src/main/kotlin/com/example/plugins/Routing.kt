package com.example.plugins
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    val poolInfoController: PoolInfoController = PoolInfoController()
    val simpleModbusRTURelay:SimpleModbusRTURelay = SimpleModbusRTURelay();
    routing {
        get("/pool-info"){
            return@get call.respondText(poolInfoController.answerServer()!!, status = HttpStatusCode.OK)
        }
        post("/test"){         /// testing post
            println("test work")
            return@post call.respondText("Test work rezalt - " +  simpleModbusRTURelay.onRelay(1) , status = HttpStatusCode.OK)
        }
        post ("/relay/0/1"){
            println("turn on channel 0")
            // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 1        r 1
            return@post call.respondText("Test work rezalt - " +  simpleModbusRTURelay.onRelay(1) , status = HttpStatusCode.OK)
        }
        post ("/relay/0/0"){
            println("turn off channel 0")
            //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 0     r 1
            return@post call.respondText("Test work rezalt - " +  simpleModbusRTURelay.offRelay(1) , status = HttpStatusCode.OK)
        }
        post ("/relay/5/1"){
            println("turn on channel 5")
            //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 1        r4
            return@post call.respondText("Test work rezalt - " +  simpleModbusRTURelay.onRelay(4) , status = HttpStatusCode.OK)
        }
        post ("/relay/5/0"){
            println("turn off channel 5")
            //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 0       r 4
            return@post call.respondText("Test work rezalt - " +  simpleModbusRTURelay.offRelay(4) , status = HttpStatusCode.OK)
        }
        post ("/relay/256/1"){
            println("turn on all channels")
            // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 1    r 256
            return@post call.respondText("Test work rezalt - " +  simpleModbusRTURelay.onRelay(256) , status = HttpStatusCode.OK)

//            or simpleModbusRTURelay.onRelay(2)
//               simpleModbusRTURelay.onRelay(5)
//               simpleModbusRTURelay.onRelay(6)
        }
        post ("/relay/256/0"){
            println("turn off all channels")
            // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 0      r 256
            return@post call.respondText("Test work rezalt - " +  simpleModbusRTURelay.offRelay(256) , status = HttpStatusCode.OK)

            //            or simpleModbusRTURelay.offRelay(2)
//                           simpleModbusRTURelay.offRelay(5)
//                           simpleModbusRTURelay.offRelay(6)
        }

    }
}
