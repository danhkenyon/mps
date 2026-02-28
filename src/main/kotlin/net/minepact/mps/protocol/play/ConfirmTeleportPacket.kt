package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class ConfirmTeleportPacket : Packet {
    override val packetId: Int = 0x00
    private var teleportId: Int = 0

    override suspend fun read(buffer: PacketBuffer) {
        teleportId = buffer.readVarInt()
    }
    override suspend fun handle(connection: Connection) {
        println("ConfirmTeleport: id=$teleportId player=${connection.player?.username}")
    }
}