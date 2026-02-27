package net.minepact.mps.registry

class RegistryManager {
    val dimensionType = Registry<Any>("minecraft:dimension_type")
    val biome = Registry<Any>("minecraft:worldgen/biome")
    val chatType = Registry<Any>("minecraft:chat_type")
    val damageType = Registry<Any>("minecraft:damage_type")

    fun all(): List<Registry<*>> {
        return listOf(
            dimensionType,
            biome,
            chatType,
            damageType
        )
    }
}