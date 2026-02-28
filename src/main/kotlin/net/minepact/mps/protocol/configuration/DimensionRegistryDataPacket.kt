package net.minepact.mps.protocol.configuration

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class DimensionRegistryDataPacket : Packet {
    override val packetId: Int = 0x05

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeString("minecraft:dimension_type")
            buf.writeVarInt(1) // only 1 entry

            buf.writeString("minecraft:overworld")
            buf.writeBoolean(true)

            buf.writeUnnamedCompound {
                writeNbtBoolean("piglin_safe", false)
                writeNbtBoolean("natural", true)
                writeNbtFloat("ambient_light", 0f)
                writeNbtBoolean("has_skylight", true)
                writeNbtBoolean("has_ceiling", false)
                writeNbtBoolean("ultrawarm", false)
                writeNbtBoolean("bed_works", true)
                writeNbtBoolean("respawn_anchor_works", false)
                writeNbtInt("min_y", -64)
                writeNbtInt("height", 384)
                writeNbtInt("logical_height", 384)
                writeNbtDouble("coordinate_scale", 1.0)
                writeNbtString("infiniburn", "#minecraft:infiniburn_overworld")
                writeNbtString("effects", "minecraft:overworld")
                writeNbtInt("monster_spawn_light_level", 0)
                writeNbtInt("monster_spawn_block_light_limit", 0)
            }
        }
    }
}