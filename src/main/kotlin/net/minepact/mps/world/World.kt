package net.minepact.mps.world

data class World(
    val name: String,
    val dimensionName: String,
    val seed: Long,
    val spawnX: Double,
    val spawnY: Double,
    val spawnZ: Double,
    val spawnYaw: Float = 0f,
    val spawnPitch: Float = 0f
)