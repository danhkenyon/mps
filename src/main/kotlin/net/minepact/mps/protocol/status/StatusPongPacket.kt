package net.minepact.mps.protocol.status

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class StatusPongPacket(
    private val payload: Long
) : Packet {
    override val packetId: Int = 0x01

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { it.writeLong(payload) }
    }
}