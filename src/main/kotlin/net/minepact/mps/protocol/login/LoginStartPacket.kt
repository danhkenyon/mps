package net.minepact.mps.protocol.login

import net.minepact.mps.core.version.VersionRegistry
import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import net.minepact.mps.protocol.ProtocolState
import net.minepact.mps.protocol.play.KeepAliveManager
import java.util.*

class LoginStartPacket : Packet {

    override val packetId: Int = 0x00

    private var username: String = ""

    override suspend fun read(buffer: PacketBuffer) {
        username = buffer.readString(16)
    }

    override suspend fun handle(connection: Connection) {
        println("Login attempt: $username")

        val uuid = generateOfflineUUID(username)

        LoginSuccessPacket(uuid, username).handle(connection)
        connection.setProtocolState(ProtocolState.PLAY)
        VersionRegistry.current()
            .protocol
            .handleInitialPlay(connection)

        KeepAliveManager(connection, connection.getScope()).start()
    }

    private fun generateOfflineUUID(username: String): UUID {
        val data = "OfflinePlayer:$username"
        return UUID.nameUUIDFromBytes(data.toByteArray())
    }
}