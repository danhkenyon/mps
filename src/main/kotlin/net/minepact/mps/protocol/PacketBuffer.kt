package net.minepact.mps.protocol

import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class PacketBuffer(
    private val input: InputStream,
    private val output: OutputStream
) {
    fun readVarInt(): Int = VarInt.read(input)
    fun writeVarInt(value: Int) = VarInt.write(output, value)

    fun writeString(value: String) {
        val bytes = value.toByteArray(StandardCharsets.UTF_8)
        writeVarInt(bytes.size)
        output.write(bytes)
    }
    fun writeUUID(uuid: java.util.UUID) {
        writeLong(uuid.mostSignificantBits)
        writeLong(uuid.leastSignificantBits)
    }
    fun writeInt(value: Int) {
        output.write((value shr 24) and 0xFF)
        output.write((value shr 16) and 0xFF)
        output.write((value shr 8) and 0xFF)
        output.write(value and 0xFF)
    }
    fun writeBoolean(value: Boolean) {
        output.write(if (value) 1 else 0)
    }
    fun writeShort(value: Int) {
        output.write((value shr 8) and 0xFF)
        output.write(value and 0xFF)
    }
    fun writeDouble(value: Double) {
        writeLong(java.lang.Double.doubleToLongBits(value))
    }
    fun writeFloat(value: Float) {
        writeInt(java.lang.Float.floatToIntBits(value))
    }
    fun writeLong(value: Long) {
        for (i in 7 downTo 0) {
            output.write((value shr (8 * i)).toInt() and 0xFF)
        }
    }
    fun writeByte(value: Int) = output.write(value)
    fun writeUnsignedShort(value: Int) {
        output.write((value shr 8) and 0xFF)
        output.write(value and 0xFF)
    }

    fun readString(maxLength: Int): String {
        val length = readVarInt()
        require(length <= maxLength * 4) { "String too long" }
        val bytes = ByteArray(length)
        input.read(bytes)
        val str = String(bytes, StandardCharsets.UTF_8)
        require(str.length <= maxLength) { "String exceeds max characters" }
        return str
    }
    fun readUUID(): java.util.UUID {
        val most = readLong()
        val least = readLong()
        return java.util.UUID(most, least)
    }
    fun readLong(): Long {
        var result = 0L
        repeat(8) {
            result = result shl 8
            result = result or (input.read().toLong() and 0xFF)
        }
        return result
    }
    fun readUnsignedShort(): Int {
        val high = input.read()
        val low = input.read()
        return (high shl 8) or low
    }
    fun readByte(): Byte = input.read().toByte()
}