package net.minepact.mps.world.dimension

object Dimensions {
    val OVERWORLD = DimensionType(
        id = 0,
        name = "minecraft:overworld",
        natural = true,
        hasSkylight = true,
        hasCeiling = false,
        ambientLight = 0.0f
    )

    val ALL = listOf(OVERWORLD)
}