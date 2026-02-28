package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class SetDefaultSpawnPositionPacket(
    private val x: Int,
    private val y: Int,
    private val z: Int,
    private val angle: Float = 0f
) : Packet {
    override val packetId: Int = 0x52

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeLong(encodePosition(x, y, z))
            buf.writeFloat(angle)
        }
    }

    private fun encodePosition(x: Int, y: Int, z: Int): Long =
                ((x.toLong() and 0x3FF_FFFF) shl 38) or
                ((z.toLong() and 0x3FF_FFFF) shl 12) or
                (y.toLong()  and 0xFFF)
}