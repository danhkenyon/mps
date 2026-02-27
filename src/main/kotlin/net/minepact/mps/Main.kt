package net.minepact.mps

import kotlinx.coroutines.runBlocking
import net.minepact.mps.core.MinePactServer

object Main {

    @JvmStatic fun main(args: Array<String>): Unit = runBlocking {
        val server = MinePactServer()
        server.start()

        Runtime.getRuntime().addShutdownHook(Thread { runBlocking {
                server.stop()
        }})

        server.awaitShutdown()
    }
}