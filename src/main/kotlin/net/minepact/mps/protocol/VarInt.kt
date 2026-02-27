package net.minepact.mps.protocol

import java.io.InputStream
import java.io.OutputStream

object VarInt {
    fun read(input: InputStream): Int {
        var numRead = 0
        var result = 0
        var read: Int

        do {
            read = input.read()
            if (read == -1) throw RuntimeException("Stream ended while reading VarInt")

            val value = (read and 0b01111111)
            result = result or (value shl (7 * numRead))

            numRead++
            if (numRead > 5) {
                throw RuntimeException("VarInt too big")
            }

        } while ((read and 0b10000000) != 0)

        return result
    }
    fun write(output: OutputStream, value: Int) {
        var v = value
        do {
            var temp = (v and 0b01111111)
            v = v ushr 7
            if (v != 0) {
                temp = temp or 0b10000000
            }
            output.write(temp)
        } while (v != 0)
    }
}