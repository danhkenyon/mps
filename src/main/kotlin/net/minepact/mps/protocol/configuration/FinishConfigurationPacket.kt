package net.minepact.mps.protocol.configuration

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class FinishConfigurationPacket : Packet {
    override val packetId: Int = 0x02

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { /* no payload */ }
        println("Sent FinishConfiguration and waiting for client acknowledge")
    }
}