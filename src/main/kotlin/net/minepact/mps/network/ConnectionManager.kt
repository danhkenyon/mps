package net.minepact.mps.network

import java.util.concurrent.ConcurrentHashMap

class ConnectionManager {
    private val connections = ConcurrentHashMap.newKeySet<Connection>()

    fun add(connection: Connection) {
        connections.add(connection)
    }
    fun remove(connection: Connection) {
        connections.remove(connection)
    }

    fun count(): Int = connections.size
    fun all(): Set<Connection> = connections
}