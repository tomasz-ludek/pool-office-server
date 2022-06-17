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
            route("/0"){
                post("/1") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 1        r 1
                    return@post call.respond(simpleModbusRTURelay.onRelay(1)) }
                post("/0") {
                    //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 1 -R /dev/ttyAMA1 0     r 1
                    return@post call.respond( simpleModbusRTURelay.offRelay(1))
                }
            }
            route("/5"){
                post("/1") {
                    //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 1        r4
                    return@post call.respond( simpleModbusRTURelay.onRelay(4))
                }
                post("/0") {
                    //mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 4 -R /dev/ttyAMA1 0       r 4
                    return@post call.respond( simpleModbusRTURelay.offRelay(4)) }
            }
            route("/256"){
                post("/1") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 1    r 256
                    return@post call.respond( simpleModbusRTURelay.onRelay(256))
//               or simpleModbusRTURelay.onRelay(2)
//               simpleModbusRTURelay.onRelay(5)
//               simpleModbusRTURelay.onRelay(6)
                }
                post("/0") {
                    // mbpoll -v -a 1 -b 9600 -m rtu -t 0 -P none -r 256 -R /dev/ttyAMA1 0      r 256
                    return@post call.respond( simpleModbusRTURelay.offRelay(256))
                //           or simpleModbusRTURelay.offRelay(2)
//                           simpleModbusRTURelay.offRelay(5)
//                           simpleModbusRTURelay.offRelay(6)
                }
            }
        }
    }
}