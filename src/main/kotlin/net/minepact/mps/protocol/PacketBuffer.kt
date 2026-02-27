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

    object NbtTag {
        const val END = 0
        const val BYTE = 1
        const val SHORT = 2
        const val INT = 3
        const val LONG = 4
        const val FLOAT = 5
        const val DOUBLE = 6
        const val BYTE_ARRAY = 7
        const val STRING = 8
        const val LIST = 9
        const val COMPOUND = 10
        const val INT_ARRAY = 11
        const val LONG_ARRAY = 12
    }

    fun writeNbtRootCompound(writeBody: PacketBuffer.() -> Unit) {
        writeByte(NbtTag.COMPOUND)
        writeShort(0) // root name length (empty)
        this.writeBody()
        writeByte(NbtTag.END)
    }

    /* ---------- Named Tags ---------- */

    private fun writeTagHeader(type: Int, name: String) {
        writeByte(type)
        writeShort(name.length)
        output.write(name.toByteArray(StandardCharsets.UTF_8))
    }
    fun writeNbtString(name: String, value: String) {
        writeTagHeader(NbtTag.STRING, name)
        writeShort(value.length)
        output.write(value.toByteArray(StandardCharsets.UTF_8))
    }
    fun writeNbtInt(name: String, value: Int) {
        writeTagHeader(NbtTag.INT, name)
        writeInt(value)
    }
    fun writeNbtFloat(name: String, value: Float) {
        writeTagHeader(NbtTag.FLOAT, name)
        writeFloat(value)
    }
    fun writeNbtDouble(name: String, value: Double) {
        writeTagHeader(NbtTag.DOUBLE, name)
        writeDouble(value)
    }
    fun writeNbtBoolean(name: String, value: Boolean) {
        writeTagHeader(NbtTag.BYTE, name)
        writeByte(if (value) 1 else 0)
    }
    fun writeNbtCompound(name: String, writeBody: PacketBuffer.() -> Unit) {
        writeTagHeader(NbtTag.COMPOUND, name)
        this.writeBody()
        writeByte(NbtTag.END)
    }

    fun writeNbtList(name: String, elementType: Int, elements: List<PacketBuffer.() -> Unit>) {
        writeTagHeader(NbtTag.LIST, name)
        writeByte(elementType)
        writeInt(elements.size)

        for (element in elements) {
            this.element()
        }
    }
    fun writeUnnamedCompound(writeBody: PacketBuffer.() -> Unit) {
        writeByte(NbtTag.COMPOUND)
        writeShort(0)
        this.writeBody()
        writeByte(NbtTag.END)
    }
}