package net.minepact.mps.protocol

import net.minepact.mps.network.Connection

interface Packet {
    val packetId: Int

    suspend fun read(buffer: PacketBuffer)
    suspend fun handle(connection: Connection)
}