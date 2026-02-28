package net.minepact.mps.protocol.play

import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.PacketBuffer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ChunkDataAndUpdateLightPacket(
    private val chunkX: Int,
    private val chunkZ: Int
) : Packet {
    override val packetId: Int = 0x24

    override suspend fun read(buffer: PacketBuffer) {}
    override suspend fun handle(connection: Connection) {
        connection.sendPacket(packetId) { buf ->
            buf.writeInt(chunkX)
            buf.writeInt(chunkZ)

            val emptyHeightmap = LongArray(36)
            buf.writeNbtRootCompound {
                writeNbtLongArray("MOTION_BLOCKING", emptyHeightmap)
                writeNbtLongArray("WORLD_SURFACE",   emptyHeightmap)
            }

            buf.writeByteArray(buildSectionData())
            buf.writeVarInt(0)
            writeLightData(buf)
        }
    }

    private fun buildSectionData(): ByteArray {
        val out = ByteArrayOutputStream()
        val buf = PacketBuffer(ByteArrayInputStream(ByteArray(0)), out)

        repeat(SECTION_COUNT) {
            buf.writeShort(0) // non-empty block count

            // block states
            buf.writeByte(0) // bits per entry
            buf.writeVarInt(0) // value = air (block state 0)
            buf.writeVarInt(0) // data length

            // biomes (just plains for now)
            buf.writeByte(0) // bits per entry
            buf.writeVarInt(0) // value = plains (biome id 0)
            buf.writeVarInt(0) // data length
        }

        return out.toByteArray()
    }

    private fun writeLightData(buf: PacketBuffer) {
        val allSections = (1L shl LIGHT_SECTION_COUNT) - 1L
        val noSections = 0L

        buf.writeBitSet(allSections)  // sky light mask (all lit)
        buf.writeBitSet(noSections) // no block light mask
        buf.writeBitSet(noSections) // no sky light mask
        buf.writeBitSet(allSections)  // no block light mask (all 0)

        // maximum brightness
        val fullLight = ByteArray(2048) { 0xFF.toByte() }
        buf.writeVarInt(LIGHT_SECTION_COUNT)
        repeat(LIGHT_SECTION_COUNT) { buf.writeByteArray(fullLight) }
        buf.writeVarInt(0) // block light arrays
    }

    companion object {
        private const val SECTION_COUNT = 24
        private const val LIGHT_SECTION_COUNT = SECTION_COUNT + 2 // 26
    }
}