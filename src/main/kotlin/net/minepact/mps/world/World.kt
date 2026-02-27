package net.minepact.mps.world

import net.minepact.mps.world.dimension.DimensionType
import net.minepact.mps.world.level.SpawnPoint

class World(
    val name: String,
    val dimension: DimensionType,
    val spawnPoint: SpawnPoint,
    val seed: Long
)