package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class PlayerPositionPacket : Packet {

    override val packetId: Int = 0x39

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {

        connection.sendPacket(packetId) { buf ->
            buf.writeDouble(0.0)
            buf.writeDouble(100.0)
            buf.writeDouble(0.0)

            buf.writeFloat(0f)
            buf.writeFloat(0f)

            buf.writeByte(0)  // Flags
            buf.writeVarInt(0) // Teleport ID
        }
    }
}