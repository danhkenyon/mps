package net.minepact.mps.registry.builtin

import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.registry.core.Registry

fun PacketBuffer.writeDimensionRegistry(
    registry: Registry<DimensionType>
) {
    writeNbtCompound("minecraft:dimension_type") {
        writeNbtString("type", "minecraft:dimension_type")
        writeNbtList(
            name = "value",
            elementType = PacketBuffer.NbtTag.COMPOUND,
            elements = registry.entries().map { entry ->
                {
                    writeNbtString("name", entry.key.value)
                    writeNbtInt("id", entry.id)

                    writeNbtCompound("element") {
                        writeNbtBoolean("piglin_safe", entry.value.piglinSafe)
                        writeNbtBoolean("natural", entry.value.natural)
                        writeNbtFloat("ambient_light", entry.value.ambientLight)
                        writeNbtBoolean("has_skylight", entry.value.hasSkylight)
                        writeNbtBoolean("has_ceiling", entry.value.hasCeiling)
                        writeNbtBoolean("ultrawarm", entry.value.ultrawarm)
                        writeNbtBoolean("bed_works", entry.value.bedWorks)
                        writeNbtBoolean("respawn_anchor_works", entry.value.respawnAnchorWorks)
                        writeNbtInt("min_y", entry.value.minY)
                        writeNbtInt("height", entry.value.height)
                        writeNbtInt("logical_height", entry.value.logicalHeight)
                        writeNbtDouble("coordinate_scale", entry.value.coordinateScale)
                        writeNbtString("infiniburn", entry.value.infiniburn)
                        writeNbtString("effects", entry.value.effects)
                    }
                }
            }
        )
    }
}