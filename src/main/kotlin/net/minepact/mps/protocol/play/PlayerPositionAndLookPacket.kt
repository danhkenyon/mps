package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import java.util.concurrent.atomic.AtomicInteger

class PlayerPositionAndLookPacket(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) : Packet {
    override val packetId: Int = 0x3E // 1.20.4 clientbound

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->

            buf.writeDouble(x)
            buf.writeDouble(y)
            buf.writeDouble(z)
            buf.writeFloat(yaw)
            buf.writeFloat(pitch)

            buf.writeByte(0x00) // Flags (0 = absolute positioning)

            buf.writeVarInt(teleportId.incrementAndGet())

            buf.writeBoolean(false) // Dismount vehicle
        }
    }

    companion object {
        private val teleportId = AtomicInteger(0)
    }
}