package net.minepact.mps.core

import kotlinx.coroutines.*
import net.minepact.mps.core.version.MinecraftVersion
import net.minepact.mps.core.version.VersionRegistry
import net.minepact.mps.core.version.v1_20_4.Protocol_1_20_4
import java.util.concurrent.atomic.AtomicReference
import net.minepact.mps.network.NetworkServer
import net.minepact.mps.player.PlayerManager
import net.minepact.mps.world.WorldManager

class MinecraftServer(
    val config: ServerConfig = ServerConfig()
) {
    private lateinit var networkServer: NetworkServer

    val worldManager = WorldManager()
    val playerManager = PlayerManager()

    private val lifecycle = AtomicReference(Lifecycle.CREATED)
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    suspend fun start() {
        if (!lifecycle.compareAndSet(Lifecycle.CREATED, Lifecycle.STARTING)) {
            error("The server has started or is already starting!")
        }

        println("Starting Server...")
        println("Offline mode: ${!config.onlineMode}")

        initializeCore()
        worldManager.initialize()

        lifecycle.set(Lifecycle.RUNNING)
        println("Server is now running.")
    }

    private suspend fun initializeCore() {
        VersionRegistry.register(
            MinecraftVersion(
                name = "1.20.4",
                protocolVersion = 765,
                dataVersion = 3700,
                protocol = Protocol_1_20_4()
            )
        )
        VersionRegistry.freeze()

        networkServer = NetworkServer(config.host, config.port, scope, this)
        networkServer.start()
    }

    suspend fun stop() {
        if (!lifecycle.compareAndSet(Lifecycle.RUNNING, Lifecycle.STOPPING)) return

        println("Stopping MinePact Server...")
        networkServer.stop()
        job.cancelAndJoin()

        lifecycle.set(Lifecycle.STOPPED)
        println("Server stopped.")
    }

    suspend fun awaitShutdown() {
        while (lifecycle.get() != Lifecycle.STOPPED) {
            delay(100)
        }
    }

    fun getScope(): CoroutineScope = scope
    fun getLifecycle(): Lifecycle = lifecycle.get()
}