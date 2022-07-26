package pl.ludek.poolserver.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    val poolInfoController = PoolInfoController()
    val simpleModbusRTURelay = SimpleModbusRTURelay()
    routing {
        get("/pool-info"){
            return@get call.respond(poolInfoController.answerServer())
        }
        route("/relay"){
            get("/state"){return@get call.respond(simpleModbusRTURelay.getStateAllRelay())}
            route("/0"){
                post("/1") {
                    return@post call.respond(simpleModbusRTURelay.onRelay(1)) }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(1))
                }
            }
            route("/1"){
                post("/1") {
                    return@post call.respond(simpleModbusRTURelay.onRelay(2)) }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(2))
                }
            }
            route("/2"){
                post("/1") {
                    return@post call.respond(simpleModbusRTURelay.onRelay(3)) }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(3))
                }
            }
            route("/3"){
                post("/1") {
                    return@post call.respond(simpleModbusRTURelay.onRelay(4)) }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(4))
                }
            }
            route("/4"){
                post("/1") {
                    return@post call.respond(simpleModbusRTURelay.onRelay(5)) }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(5))
                }
            }
            route("/5"){
                post("/1") {
                    return@post call.respond( simpleModbusRTURelay.onRelay(6))
                }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(6)) }
            }
            route("/6"){
                post("/1") {
                    return@post call.respond(simpleModbusRTURelay.onRelay(7)) }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(7))
                }
            }
            route("/7"){
                post("/1") {
                    return@post call.respond(simpleModbusRTURelay.onRelay(8)) }
                post("/0") {
                    return@post call.respond( simpleModbusRTURelay.offRelay(8))
                }
            }
            route("/256"){
                post("/1") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 1    r 256
                    return@post call.respond( simpleModbusRTURelay.onRelay(256))
                }
                post("/0") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 0      r 256
                    return@post call.respond( simpleModbusRTURelay.offRelay(256))
                }
            }
        }
    }
}