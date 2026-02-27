package net.minepact.mps.world

import net.minepact.mps.world.dimension.Dimensions
import net.minepact.mps.world.level.SpawnPoint
import java.util.concurrent.atomic.AtomicBoolean

class WorldManager {
    private val worlds = mutableMapOf<String, World>()
    private val frozen = AtomicBoolean(false)

    fun initialize() {
        if (frozen.get()) return
        val overworld = World(
            name = "world",
            dimension = Dimensions.OVERWORLD,
            spawnPoint = SpawnPoint(
                x = 0.0,
                y = 64.0,
                z = 0.0,
                yaw = 0f,
                pitch = 0f
            ),
            seed = 0L
        )

        worlds[overworld.name] = overworld
        frozen.set(true)
    }

    fun getWorld(name: String): World? = worlds[name]
    fun getDefaultWorld(): World = worlds["world"] ?: throw IllegalStateException("Default world not initialized")

    fun isFrozen(): Boolean = frozen.get()
}