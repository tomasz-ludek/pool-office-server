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
        post("/test"){
            simpleModbusRTURelay.setTest1()

            println("test work")
            return@post call.respondText("Test work" , status = HttpStatusCode.OK)
        }
        post ("/relay/0/1"){
            println("turn on channel 0")
            // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 1
        }
        post ("/relay/0/0"){
            println("turn off channel 0")
            //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 0
        }
        post ("/relay/5/1"){
            println("turn on channel 5")
            //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 1
        }
        post ("/relay/5/0"){
            println("turn off channel 5")
            //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 0
        }
        post ("/relay/256/1"){
            println("turn on all channels")
            // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 1
        }
        post ("/relay/256/0"){
            println("turn off all channels")
            // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 0
        }

    }
}


















