package net.minepact.mps.protocol.configuration

import net.minepact.mps.core.version.VersionRegistry
import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.protocol.ProtocolState
import net.minepact.mps.protocol.play.KeepAliveManager

class AcknowledgeFinishConfigurationPacket : Packet {
    override val packetId: Int = 0x02

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        println("AcknowledgeFinishConfiguration received - entering PLAY state")
        connection.setProtocolState(ProtocolState.PLAY)

        VersionRegistry.current().protocol.handleInitialPlay(connection) // send login
        KeepAliveManager(connection, connection.getScope()).start() // inits the loop

        val player = connection.player
        println("Player ${player?.username} is now in-game (entityId=${player?.entityId})")
    }
}