package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class PlayerRotationPacket : Packet {
    override val packetId: Int = 0x16

    private var yaw = 0f
    private var pitch = 0f
    private var onGround = false

    override suspend fun read(buffer: PacketBuffer) {
        yaw = buffer.readFloat()
        pitch = buffer.readFloat()
        onGround = buffer.readBoolean()
    }

    override suspend fun handle(connection: Connection) {
        // Future: update connection.player rotation.
    }
}