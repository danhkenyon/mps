package net.minepact.mps.registry.builtin

import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.registry.core.Registry

fun PacketBuffer.writeBiomeRegistry(
    registry: Registry<Biome>
) {
    writeNbtCompound("minecraft:worldgen/biome") {
        writeNbtString("type", "minecraft:worldgen/biome")
        writeNbtList(
            name = "value",
            elementType = PacketBuffer.NbtTag.COMPOUND,
            elements = registry.entries().map { entry ->
                {
                    writeNbtString("name", entry.key.value)
                    writeNbtInt("id", entry.id)

                    writeNbtCompound("element") {
                        writeNbtFloat("temperature", entry.value.temperature)
                        writeNbtFloat("downfall", entry.value.downfall)

                        writeNbtCompound("effects") {
                            writeNbtInt("sky_color", 7907327)
                            writeNbtInt("water_fog_color", 329011)
                            writeNbtInt("fog_color", 12638463)
                            writeNbtInt("water_color", 4159204)
                        }
                    }
                }
            }
        )
    }
}