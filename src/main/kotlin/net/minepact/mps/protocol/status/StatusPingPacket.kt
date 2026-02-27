package net.minepact.mps.protocol.status

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class StatusPingPacket : Packet {
    override val packetId: Int = 0x01
    private var payload: Long = 0

    override suspend fun read(buffer: PacketBuffer) {
        payload = buffer.readLong()
    }
    override suspend fun handle(connection: Connection) {
        StatusPongPacket(payload).handle(connection)
    }
}