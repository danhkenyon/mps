package net.minepact.mps.registry.builtin

data class DimensionType(
    val fixedTime: Long? = null,
    val hasSkylight: Boolean = true,
    val hasCeiling: Boolean = false,
    val ultrawarm: Boolean = false,
    val natural: Boolean = true,
    val coordinateScale: Double = 1.0,
    val piglinSafe: Boolean = false,
    val bedWorks: Boolean = true,
    val respawnAnchorWorks: Boolean = false,
    val minY: Int = -64,
    val height: Int = 384,
    val logicalHeight: Int = 384,
    val infiniburn: String = "#minecraft:infiniburn_overworld",
    val effects: String = "minecraft:overworld",
    val ambientLight: Float = 0f
)