package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer

class JoinGamePacket(
    val entityId: Int,
    val isHardcore: Boolean,
    val gameMode: Int,
    val dimensionName: String,
    val hashedSeed: Long,
    val spawnX: Double,
    val spawnY: Double,
    val spawnZ: Double
) : Packet {
    override val packetId: Int = 0x28

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeInt(entityId) // entity id
            buf.writeBoolean(isHardcore) // hardcore
            buf.writeVarInt(1) // dim count
            buf.writeString(dimensionName) // dim names
            buf.writeVarInt(20) // max players
            buf.writeVarInt(10) // view distance
            buf.writeVarInt(10) // simulation distance
            buf.writeBoolean(false) // reduced debug info
            buf.writeBoolean(true) // enable respawn screen
            buf.writeBoolean(false) // limited crafting
            buf.writeString(dimensionName) // dim type (MUST match register type)
            buf.writeString(dimensionName) // dim name
            buf.writeLong(hashedSeed) // seed but hash brown???????
            buf.writeByte(gameMode) // gamemode
            buf.writeByte(-1) // prev gamemode (-1 = none)
            buf.writeBoolean(false) // is debug
            buf.writeBoolean(false) // is flat
            buf.writeBoolean(false) // has death loc
            buf.writeVarInt(0) // portal cooldown
        }
    }
}