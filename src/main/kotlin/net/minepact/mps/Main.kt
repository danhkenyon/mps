package net.minepact.mps

import kotlinx.coroutines.runBlocking
import net.minepact.mps.core.MinecraftServer

object Main {

    @JvmStatic fun main(args: Array<String>): Unit = runBlocking {
        val server = MinecraftServer()
        server.start()

        Runtime.getRuntime().addShutdownHook(Thread { runBlocking {
                server.stop()
        }})

        server.awaitShutdown()
    }
}