package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class PlayerAbilitiesPacket(
    private val flags: Int,
    private val flyingSpeed: Float = 0.05f,
    private val fovModifier: Float = 0.1f
) : Packet {
    override val packetId: Int = 0x34

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeByte(flags)
            buf.writeFloat(flyingSpeed)
            buf.writeFloat(fovModifier)
        }
    }

    companion object {
        fun creative() = PlayerAbilitiesPacket(flags = 0x01 or 0x04 or 0x08)
        fun survival() = PlayerAbilitiesPacket(flags = 0x00)
    }
}