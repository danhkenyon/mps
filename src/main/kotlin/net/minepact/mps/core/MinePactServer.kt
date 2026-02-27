package net.minepact.mps.core

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicReference
import net.minepact.mps.network.NetworkServer
import net.minepact.mps.registry.RegistryManager
import net.minepact.mps.registry.biome.Biome
import net.minepact.mps.registry.dimension.DimensionType

class MinePactServer(
    val config: ServerConfig = ServerConfig()
) {
    private lateinit var networkServer: NetworkServer

    private val lifecycle = AtomicReference(Lifecycle.CREATED)
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    suspend fun start() {
        if (!lifecycle.compareAndSet(Lifecycle.CREATED, Lifecycle.STARTING)) {
            error("The server has started or is already starting!")
        }

        println("Starting MinePact Server...")
        println("Offline mode: ${!config.onlineMode}")

        initializeCore()

        lifecycle.set(Lifecycle.RUNNING)
        println("MinePact Server is now running.")
    }
    private suspend fun initializeCore() {
        networkServer = NetworkServer(
            config.host,
            config.port,
            scope
        )

        networkServer.start()

        val registryManager = RegistryManager()

        registryManager.dimensionType.register(
            "minecraft:overworld",
            DimensionType()
        )
        registryManager.biome.register(
            "minecraft:plains",
            Biome(temperature = 0.8f, downfall = 0.4f)
        )

        // load worlds, load registries
    }

    suspend fun stop() {
        if (!lifecycle.compareAndSet(Lifecycle.RUNNING, Lifecycle.STOPPING)) {
            return
        }

        println("Stopping MinePact Server...")

        shutdownCore()

        job.cancelAndJoin()

        lifecycle.set(Lifecycle.STOPPED)
        println("Server stopped.")
    }
    private suspend fun shutdownCore() {
        networkServer.stop()
        /// save worlds, dc players
    }
    suspend fun awaitShutdown() {
        while (lifecycle.get() != Lifecycle.STOPPED) {
            delay(100)
        }
    }

    fun getScope(): CoroutineScope = scope
    fun getLifecycle(): Lifecycle = lifecycle.get()
}