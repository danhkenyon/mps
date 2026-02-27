package net.minepact.mps.protocol.handshake

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.protocol.ProtocolState

class HandshakePacket : Packet {
    override val packetId: Int = 0x00

    var protocolVersion: Int = 0
    var serverAddress: String = ""
    var serverPort: Int = 0
    var nextState: Int = 0

    override suspend fun read(buffer: PacketBuffer) {
        protocolVersion = buffer.readVarInt()
        serverAddress = buffer.readString(32)
        serverPort = buffer.readUnsignedShort()
        nextState = buffer.readVarInt()
    }
    override suspend fun handle(connection: Connection) {
        println("Handshake:")
        println(" Protocol: $protocolVersion")
        println(" Address: $serverAddress")
        println(" Port: $serverPort")
        println(" NextState: $nextState")

        connection.setProtocolState(
            when (nextState) {
                1 -> ProtocolState.STATUS
                2 -> ProtocolState.LOGIN
                else -> ProtocolState.HANDSHAKE
            }
        )
    }
}