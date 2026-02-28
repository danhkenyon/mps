package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class GameEventPacket(
    private val event: Int,
    private val value: Float = 0f
) : Packet {
    override val packetId: Int = 0x20

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeByte(event)
            buf.writeFloat(value)
        }
    }

    companion object {
        fun startWaitingForChunks() = GameEventPacket(event = 13, value = 0f)
    }
}