package pl.ludek.poolserver

import io.ktor.server.application.*
import pl.ludek.poolserver.plugins.*
import io.ktor.server.netty.*
import pl.ludek.poolserver.plugins.configureRouting

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureRouting()
   // configureSerialization()
}
