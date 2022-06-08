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
           // return@get call.respondText(poolInfoController.answerServer().toString(), ContentType.Application.Json, status = HttpStatusCode.OK)
            return@get call.respond(poolInfoController.answerServer())
        }
        route("/relay"){
            route("/0"){
                post("/1") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 1        r 1
                    return@post call.respondText(simpleModbusRTURelay.onRelay(1) ,ContentType.Application.Json,  status = HttpStatusCode.OK) }
                post("/0") {
                    //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 0     r 1
                    return@post call.respondText( simpleModbusRTURelay.offRelay(1) , ContentType.Application.Json,  status = HttpStatusCode.OK)
                }
            }
            route("/5"){
                post("/1") {
                    //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 1        r4
                    return@post call.respondText( simpleModbusRTURelay.onRelay(4) , ContentType.Application.Json,  status = HttpStatusCode.OK)
                }
                post("/0") {
                    //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 0       r 4
                    return@post call.respondText( simpleModbusRTURelay.offRelay(4) , ContentType.Application.Json,  status = HttpStatusCode.OK) }
            }
            route("/256"){
                post("/1") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 1    r 256
                    return@post call.respondText( simpleModbusRTURelay.onRelay(256) , ContentType.Application.Json,  status = HttpStatusCode.OK)
//            or simpleModbusRTURelay.onRelay(2)
//               simpleModbusRTURelay.onRelay(5)
//               simpleModbusRTURelay.onRelay(6)
                }
                post("/0") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 0      r 256
                    return@post call.respondText( simpleModbusRTURelay.offRelay(256) , ContentType.Application.Json, status = HttpStatusCode.OK)
                //            or simpleModbusRTURelay.offRelay(2)
//                           simpleModbusRTURelay.offRelay(5)
//                           simpleModbusRTURelay.offRelay(6)
                }
            }
        }
    }
}
