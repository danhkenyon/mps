package net.minepact.mps.world

class WorldManager {
    private val worlds = mutableMapOf<String, World>()

    fun initialize() {
        val overworld = World(
            name          = "world",
            dimensionName = "minecraft:overworld",
            seed          = 0L,
            spawnX        = 0.0,
            spawnY        = 64.0,
            spawnZ        = 0.0
        )
        worlds[overworld.name] = overworld
    }

    fun getWorld(name: String): World? = worlds[name]
    fun getDefaultWorld(): World = worlds["world"] ?: error("Default world not initialized — call initialize() first")
}