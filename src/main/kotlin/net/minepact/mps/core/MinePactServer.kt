package net.minepact.mps.core

import kotlinx.coroutines.*
import net.minepact.mps.core.version.MinecraftVersion
import net.minepact.mps.core.version.VersionRegistry
import net.minepact.mps.core.version.v1_20_4.Protocol_1_20_4
import java.util.concurrent.atomic.AtomicReference
import net.minepact.mps.network.NetworkServer
import net.minepact.mps.player.PlayerManager
import net.minepact.mps.registry.builtin.Biome
import net.minepact.mps.registry.core.RegistryManager
import net.minepact.mps.world.WorldManager
import net.minepact.mps.registry.builtin.DimensionType
import net.minepact.mps.registry.core.RegistryKey

class MinePactServer(
    val config: ServerConfig = ServerConfig()
) {
    private lateinit var networkServer: NetworkServer
    private lateinit var playerManager: PlayerManager

    val worldManager: WorldManager = WorldManager()
    val registryManager = RegistryManager()
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
        worldManager.initialize()

        lifecycle.set(Lifecycle.RUNNING)
        println("MinePact Server is now running.")
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
        initializeRegistries()
        registryManager.freeze()

        playerManager = PlayerManager()
        networkServer = NetworkServer(
            config.host,
            config.port,
            scope,
            this
        )

        networkServer.start()
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

    private fun initializeRegistries() {
        val dimensionRegistry = registryManager.create<DimensionType>("minecraft:dimension_type")
        val biomeRegistry = registryManager.create<Biome>("minecraft:worldgen/biome")

        dimensionRegistry.register(
            RegistryKey("minecraft:overworld"),
            DimensionType()
        )
        biomeRegistry.register(
            RegistryKey("minecraft:plains"),
            Biome(
                temperature = 0.8f,
                downfall = 0.4f
            )
        )
    }

    fun getScope(): CoroutineScope = scope
    fun getLifecycle(): Lifecycle = lifecycle.get()
    fun getPlayerManager(): PlayerManager = playerManager
}