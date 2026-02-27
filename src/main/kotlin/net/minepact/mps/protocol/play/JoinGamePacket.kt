package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.registry.builtin.Biome
import net.minepact.mps.registry.builtin.DimensionType
import net.minepact.mps.registry.builtin.writeBiomeRegistry
import net.minepact.mps.registry.builtin.writeDimensionRegistry

class JoinGamePacket(
    val entityId: Int,
    val isHardcore: Boolean,
    val gameMode: Int,

    val dimensionName: String,
    val hashedSeed: Long,
    val spawnX: Double,
    val spawnY: Double,
    val spawnZ: Double

) : Packet {
    override val packetId: Int = 0x28

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->

            // --- Player Info ---
            buf.writeInt(entityId)
            buf.writeBoolean(isHardcore)
            buf.writeByte(gameMode)
            buf.writeByte(-1) // Previous Gamemode

            // --- World List ---
            buf.writeVarInt(1)
            buf.writeString(dimensionName)

            // --- Registry Codec ---
            writeRegistryCodec(buf, connection)

            // --- Dimension Info ---
            buf.writeString(dimensionName) // Dimension Type
            buf.writeString(dimensionName) // Dimension Name

            // --- World Settings ---
            buf.writeLong(hashedSeed)
            buf.writeVarInt(20)
            buf.writeVarInt(12)
            buf.writeVarInt(12)

            buf.writeBoolean(false)
            buf.writeBoolean(true)
            buf.writeBoolean(false)
            buf.writeBoolean(false)

            buf.writeBoolean(false) // Last death location
            buf.writeVarInt(0)      // Portal cooldown
        }
    }

    private fun writeRegistryCodec(
        buffer: PacketBuffer,
        connection: Connection
    ) {
        val registryManager = connection.getServer().registryManager

        val dimensionRegistry = registryManager.get<DimensionType>("minecraft:dimension_type")
        val biomeRegistry = registryManager.get<Biome>("minecraft:worldgen/biome")

        buffer.writeNbtRootCompound {
            writeDimensionRegistry(dimensionRegistry)
            writeBiomeRegistry(biomeRegistry)
        }
    }
}