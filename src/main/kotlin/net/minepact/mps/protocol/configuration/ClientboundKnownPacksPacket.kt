package net.minepact.mps.protocol.configuration

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class ClientboundKnownPacksPacket : Packet {
    override val packetId: Int = 0x03

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeVarInt(0) // empty because something broke if it wasnt
        }
    }
}