package net.minepact.mps.protocol.configuration

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class FeatureFlagsPacket : Packet {
    override val packetId: Int = 0x0A

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeVarInt(1)
            buf.writeString("minecraft:vanilla")
        }
    }
}