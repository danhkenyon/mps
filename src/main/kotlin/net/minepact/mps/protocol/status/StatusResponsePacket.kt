package net.minepact.mps.protocol.status

import net.minepact.mps.core.version.VersionRegistry
import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class StatusResponsePacket : Packet {
    override val packetId: Int = 0x00

    override suspend fun read(buffer: PacketBuffer) {} // never gets read
    override suspend fun handle(connection: Connection) {
        val version = VersionRegistry.current()
        val json = """
        {
          "version": { "name": "${version.name}", "protocol": ${version.protocolVersion} },
          "players": { "max": 20, "online": 0 },
          "description": { "text": "MinePact Server" }
        }
        """.trimIndent().replace("\n", "").replace("  ", "")

        connection.sendPacket(packetId) { it.writeString(json) }
    }
}