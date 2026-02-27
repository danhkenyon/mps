package net.minepact.mps.protocol.play

import kotlinx.coroutines.*
import net.minepact.mps.network.Connection
import java.util.*

class KeepAliveManager(
    private val connection: Connection,
    private val scope: CoroutineScope
) {
    fun start() {
        scope.launch {
            while (true) {
                delay(10000)

                val id = Random().nextLong()
                KeepAlivePacket(id).handle(connection)
            }
        }
    }
}