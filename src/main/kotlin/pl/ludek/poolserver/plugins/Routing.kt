package pl.ludek.poolserver.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val poolInfoController = PoolInfoController()
    val simpleModbusRTURelay = SimpleModbusRTURelay()
    routing {
        get("/pool-info") {
            return@get call.respond(poolInfoController.answerServer())
        }
        route("/relay") {
            get("/state") {
                return@get call.respond(simpleModbusRTURelay.getStateAllRelay())
            }
            post("/{relay}/{state}") {
                val relay = call.parameters["relay"]!!.toInt()
                val state = call.parameters["state"]!!.toInt() == 1
                return@post call.respond(simpleModbusRTURelay.switchRelay(relay, state))
            }
        }
    }
}