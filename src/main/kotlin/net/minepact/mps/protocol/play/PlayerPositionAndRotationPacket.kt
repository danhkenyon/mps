package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class PlayerPositionAndRotationPacket : Packet {
    override val packetId: Int = 0x15

    private var x = 0.0
    private var y = 0.0
    private var z = 0.0
    private var yaw = 0f
    private var pitch = 0f
    private var onGround = false

    override suspend fun read(buffer: PacketBuffer) {
        x = buffer.readDouble()
        y = buffer.readDouble()
        z = buffer.readDouble()
        yaw = buffer.readFloat()
        pitch = buffer.readFloat()
        onGround = buffer.readBoolean()
    }

    override suspend fun handle(connection: Connection) {
        // update connection.player position and rotation
    }
}