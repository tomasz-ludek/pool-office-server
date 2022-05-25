package com.example.plugins
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        get("/pool-info"){
            return@get call.respondText(answerServer()!!, status = HttpStatusCode.OK)
        }
    }
}


















