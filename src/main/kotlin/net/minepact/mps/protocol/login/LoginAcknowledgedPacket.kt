package net.minepact.mps.protocol.login

import net.minepact.mps.core.version.VersionRegistry
import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.protocol.ProtocolState

class LoginAcknowledgedPacket : Packet {
    override val packetId: Int = 0x03

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        println("LoginAcknowledged received enters config state")
        connection.setProtocolState(ProtocolState.CONFIGURATION)

        VersionRegistry.current().protocol.handleConfiguration(connection)
    }
}