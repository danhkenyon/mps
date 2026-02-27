package net.minepact.mps.network

import kotlinx.coroutines.*
import net.minepact.mps.core.MinePactServer
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

class NetworkServer(
    private val host: String,
    private val port: Int,
    private val parentScope: CoroutineScope,
    private val server: MinePactServer
) {
    private val running = AtomicBoolean(false)
    private val connectionManager = ConnectionManager()
    private lateinit var serverSocket: ServerSocket
    private val scope = CoroutineScope(parentScope.coroutineContext + SupervisorJob())

    suspend fun start() {
        if (!running.compareAndSet(false, true)) return

        serverSocket = ServerSocket(port, 50)
        println("NetworkServer bound to $host:$port")

        scope.launch { acceptLoop() }
    }

    private suspend fun acceptLoop() {
        while (running.get()) {
            try {
                val socket: Socket = withContext(Dispatchers.IO) {
                    serverSocket.accept()
                }
                handleNewConnection(socket)
            } catch (e: Exception) {
                if (running.get()) {
                    println("Accept error: ${e.message}")
                }
            }
        }
    }

    private fun handleNewConnection(socket: Socket) {
        val connection = Connection(socket, scope, server) {
            connectionManager.remove(it)
            println("Disconnected: ${it.address}:${it.port}")
        }

        connectionManager.add(connection)
        println("New connection: ${connection.address}:${connection.port}")
        println("Active connections: ${connectionManager.count()}")

        connection.start()
    }

    suspend fun stop() {
        if (!running.compareAndSet(true, false)) return

        println("Shutting down NetworkServer...")

        try {
            serverSocket.close()
        } catch (_: Exception) {}

        connectionManager.all().forEach { it.disconnect() }
        scope.cancel()
    }

    fun getConnectionManager(): ConnectionManager = connectionManager
}