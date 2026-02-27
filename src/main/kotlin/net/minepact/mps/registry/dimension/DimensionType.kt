package net.minepact.mps.registry.dimension

data class DimensionType(
    val piglinSafe: Boolean = false,
    val natural: Boolean = true,
    val ambientLight: Float = 0.0f,
    val infiniburn: String = "#minecraft:infiniburn_overworld",
    val respawnAnchorWorks: Boolean = false,
    val hasSkylight: Boolean = true,
    val bedWorks: Boolean = true,
    val effects: String = "minecraft:overworld",
    val hasRaids: Boolean = true,
    val minY: Int = -64,
    val height: Int = 384,
    val logicalHeight: Int = 384,
    val coordinateScale: Double = 1.0,
    val ultrawarm: Boolean = false,
    val hasCeiling: Boolean = false
)