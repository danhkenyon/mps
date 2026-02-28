package net.minepact.mps.protocol.configuration

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class BiomeRegistryDataPacket : Packet {
    override val packetId: Int = 0x05

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeString("minecraft:worldgen/biome")
            buf.writeVarInt(1) // only plains

            buf.writeString("minecraft:plains")
            buf.writeBoolean(true)

            buf.writeUnnamedCompound {
                writeNbtBoolean("has_precipitation", true)
                writeNbtFloat("temperature",         0.8f)
                writeNbtFloat("downfall",            0.4f)

                writeNbtCompound("effects") {
                    writeNbtInt("sky_color",       7907327)
                    writeNbtInt("water_fog_color", 329011)
                    writeNbtInt("fog_color",       12638463)
                    writeNbtInt("water_color",     4159204)
                }
            }
        }
    }
}