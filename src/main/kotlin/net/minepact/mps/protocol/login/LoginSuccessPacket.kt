package net.minepact.mps.protocol.login

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import java.util.*

class LoginSuccessPacket(
    private val uuid: UUID,
    private val username: String
) : Packet {
    override val packetId: Int = 0x02

    override suspend fun read(buffer: PacketBuffer) {} // never read
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) {
            it.writeUUID(uuid)
            it.writeString(username)
            it.writeVarInt(0)
        }
    }
}