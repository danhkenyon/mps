package net.minepact.mps.protocol.configuration

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class ServerboundKnownPacksPacket : Packet {
    override val packetId: Int = 0x07

    override suspend fun read(buffer: PacketBuffer) {
        val count = buffer.readVarInt()
        repeat(count) {
            buffer.readString(32767) // namespace
            buffer.readString(32767) // id
            buffer.readString(32767) // version
        }
    }

    override suspend fun handle(connection: Connection) {
        // i basically use this as a big fix because it didnt let me join
    }
}