package com.example.plugins
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    val poolInfoController: PoolInfoController = PoolInfoController()
    routing {
        get("/pool-info"){
            return@get call.respondText(poolInfoController.answerServer()!!, status = HttpStatusCode.OK)
        }
    }
}


















