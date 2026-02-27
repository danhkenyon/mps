package net.minepact.mps.player

import kotlinx.coroutines.launch
import net.minepact.mps.network.Connection
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class Player(
    val uuid: UUID,
    val username: String,
    val connection: Connection,
    val entityId: Int
) {
    private val connected = AtomicBoolean(true)

    fun isConnected(): Boolean = connected.get()
    fun disconnect() {
        if (connected.compareAndSet(true, false)) {
            connection.getScope().launch { connection.disconnect() }
        }
    }
}