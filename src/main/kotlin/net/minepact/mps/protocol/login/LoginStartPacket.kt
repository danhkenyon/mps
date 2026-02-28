package net.minepact.mps.protocol.login

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import java.util.*

class LoginStartPacket : Packet {
    override val packetId: Int = 0x00

    private var username: String = ""

    override suspend fun read(buffer: PacketBuffer) {
        username = buffer.readString(16)

        val hasPlayerUUID = buffer.readBoolean()
        if (hasPlayerUUID) {
            buffer.readUUID() // discard bcz offline mode generates its own
        }
    }
    override suspend fun handle(connection: Connection) {
        println("Login attempt: $username")

        val uuid = generateOfflineUUID(username)
        val player = connection.getServer().playerManager.createPlayer(uuid, username)

        connection.player = player

        LoginSuccessPacket(uuid, username).handle(connection)

        println("Sent LoginSuccess to $username and awaiting LoginAcknowledged")
    }

    private fun generateOfflineUUID(username: String): UUID =
        UUID.nameUUIDFromBytes("OfflinePlayer:$username".toByteArray())
}