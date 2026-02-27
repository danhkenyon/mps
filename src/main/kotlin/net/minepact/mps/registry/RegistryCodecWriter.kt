package net.minepact.mps.registry

import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.registry.biome.Biome
import net.minepact.mps.registry.dimension.DimensionType

object RegistryCodecWriter {

    fun write(buf: PacketBuffer, manager: RegistryManager) {

        buf.writeNbtRootCompound {

            for (registry in manager.all()) {

                writeNbtCompound(registry.key) {

                    // Write registry type
                    writeNbtString("type", registry.key)

                    // Write registry value list
                    writeNbtList(
                        name = "value",
                        elementType = PacketBuffer.NbtTag.COMPOUND,
                        elements = registry.getEntries().map { entry ->

                            {
                                writeNbtString("name", entry.name)
                                writeNbtInt("id", entry.id)

                                writeNbtCompound("element") {
                                    writeElement(entry.element!!)
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    /* ========================================================= */
    /* ================ ELEMENT SERIALIZATION ================== */
    /* ========================================================= */

    private fun PacketBuffer.writeElement(element: Any) {
        when (element) {

            is DimensionType -> {
                writeNbtBoolean("piglin_safe", element.piglinSafe)
                writeNbtBoolean("natural", element.natural)
                writeNbtFloat("ambient_light", element.ambientLight)
                writeNbtString("infiniburn", element.infiniburn)
                writeNbtBoolean("respawn_anchor_works", element.respawnAnchorWorks)
                writeNbtBoolean("has_skylight", element.hasSkylight)
                writeNbtBoolean("bed_works", element.bedWorks)
                writeNbtString("effects", element.effects)
                writeNbtBoolean("has_raids", element.hasRaids)
                writeNbtInt("min_y", element.minY)
                writeNbtInt("height", element.height)
                writeNbtInt("logical_height", element.logicalHeight)
                writeNbtDouble("coordinate_scale", element.coordinateScale)
                writeNbtBoolean("ultrawarm", element.ultrawarm)
                writeNbtBoolean("has_ceiling", element.hasCeiling)
            }

            is Biome -> {
                writeNbtFloat("temperature", element.temperature)
                writeNbtFloat("downfall", element.downfall)

                // Minimal biome effects (REQUIRED by client)
                writeNbtCompound("effects") {
                    writeNbtInt("fog_color", 12638463)
                    writeNbtInt("water_color", 4159204)
                    writeNbtInt("water_fog_color", 329011)
                    writeNbtInt("sky_color", 7907327)
                }
            }

            else -> {
                // Future registry types go here
            }
        }
    }
}