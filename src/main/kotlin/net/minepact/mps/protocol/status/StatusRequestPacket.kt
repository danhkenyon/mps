package net.minepact.mps.protocol.status

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class StatusRequestPacket : Packet {
    override val packetId: Int = 0x00

    override suspend fun read(buffer: PacketBuffer) {} // no fields
    override suspend fun handle(connection: Connection) {
        // Respond with server status
        StatusResponsePacket().handle(connection)
    }
}