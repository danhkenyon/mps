package net.minepact.mps.world.dimension

data class DimensionType(
    val id: Int,
    val name: String,
    val natural: Boolean,
    val hasSkylight: Boolean,
    val hasCeiling: Boolean,
    val ambientLight: Float
)