package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class KeepAliveServerboundPacket : Packet {
    override val packetId: Int = 0x12
    private var id: Long = 0L

    override suspend fun read(buffer: PacketBuffer) {
        id = buffer.readLong()
    }
    override suspend fun handle(connection: Connection) {
        // validate id and reset a timeout-dc timer.
    }
}


