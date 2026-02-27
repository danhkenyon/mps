package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class KeepAlivePacket(
    val id: Long = System.currentTimeMillis()
) : Packet {
    override val packetId: Int = 0x23 // 1.20.4 clientbound

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeLong(id)
        }
    }
}