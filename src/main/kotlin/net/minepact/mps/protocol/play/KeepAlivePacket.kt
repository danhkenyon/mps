package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class KeepAlivePacket(
    private val id: Long
) : Packet {
    override val packetId: Int = 0x20

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) {
            it.writeLong(id)
        }
    }
}