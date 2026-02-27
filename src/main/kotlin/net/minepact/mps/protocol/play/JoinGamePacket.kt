package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.registry.RegistryCodecWriter
import java.util.*

class JoinGamePacket(
    private val entityId: Int
) : Packet {
    override val packetId: Int = 0x28

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->

            // --- Player Info ---
            buf.writeInt(entityId)         // Entity ID
            buf.writeBoolean(false)        // Hardcore
            buf.writeByte(1)               // Gamemode (1 = Creative)
            buf.writeByte(-1)              // Previous Gamemode (-1 = none)

            // --- World List ---
            buf.writeVarInt(1)             // World count
            buf.writeString("minecraft:overworld")

            // --- Registry Codec ---
            writeRegistryCodec(buf, connection)        // MUST be valid full NBT

            // --- Dimension Info ---
            buf.writeString("minecraft:overworld") // Dimension Type
            buf.writeString("minecraft:overworld") // Dimension Name

            // --- World Settings ---
            buf.writeLong(0L)              // Hashed seed
            buf.writeVarInt(20)            // Max players (ignored)
            buf.writeVarInt(12)            // View distance
            buf.writeVarInt(12)            // Simulation distance

            buf.writeBoolean(false)        // Reduced debug info
            buf.writeBoolean(true)         // Enable respawn screen
            buf.writeBoolean(false)        // Is debug world
            buf.writeBoolean(false)        // Is flat world

            // --- Optional Last Death Location ---
            buf.writeBoolean(false)        // Has last death location

            // --- Portal cooldown ---
            buf.writeVarInt(0)
        }
    }

    private fun writeRegistryCodec(buf: PacketBuffer, connection: Connection) {

    }
}