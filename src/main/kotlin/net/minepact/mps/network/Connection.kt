package net.minepact.mps.network

import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean
import net.minepact.mps.protocol.*
import net.minepact.mps.protocol.registry.PacketRegistry
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Connection(
    private val socket: Socket,
    private val scope: CoroutineScope,
    private val onDisconnect: suspend (Connection) -> Unit
) {
    private val alive = AtomicBoolean(true)

    private val input = BufferedInputStream(socket.getInputStream())
    private val output = BufferedOutputStream(socket.getOutputStream())

    private val incomingBuffer = ByteArrayOutputStream()

    private var protocolState = ProtocolState.HANDSHAKE

    val address = socket.inetAddress.hostAddress
    val port = socket.port

    fun start() {
        scope.launch {
            readLoop()
        }
    }

    private suspend fun readLoop() {
        try {
            val buffer = ByteArray(8192)

            while (alive.get()) {
                val read = withContext(Dispatchers.IO) {
                    input.read(buffer)
                }
                if (read == -1) break

                appendIncoming(buffer, read)
                processIncoming()
            }
        } catch (_: Exception) {
        } finally { disconnect() }
    }

    private fun appendIncoming(data: ByteArray, length: Int) {
        incomingBuffer.write(data, 0, length)
    }
    private suspend fun processIncoming() {
        while (true) {
            val bytes = incomingBuffer.toByteArray()
            val inputStream = ByteArrayInputStream(bytes)

            try {
                val packetLength = VarInt.read(inputStream)
                val headerSize = bytes.size - inputStream.available()

                if (inputStream.available() < packetLength) {
                    // not enough data
                    return
                }
                val packetData = ByteArray(packetLength)
                inputStream.read(packetData)

                handlePacket(packetData)

                // remove the consumed bytes
                val totalConsumed = headerSize + packetLength
                val remaining = bytes.copyOfRange(totalConsumed, bytes.size)

                incomingBuffer.reset()
                incomingBuffer.write(remaining)

            } catch (e: Exception) { return } // not enough data
        }
    }

    private suspend fun handlePacket(packetData: ByteArray) {
        val packetStream = ByteArrayInputStream(packetData)
        val buffer = PacketBuffer(packetStream, output)

        try {
            val packetId = buffer.readVarInt()
            val packet = PacketRegistry.create(protocolState, packetId) ?: return

            packet.read(buffer)
            packet.handle(this)
        } catch (e: Exception) {
            println("Packet handling error: ${e.message}")
        }
    }
    suspend fun sendPacket(packetId: Int, writer: (PacketBuffer) -> Unit) {
        if (!alive.get()) return

        val byteOut = ByteArrayOutputStream()
        val packetBuffer = PacketBuffer(ByteArrayInputStream(ByteArray(0)), byteOut)

        packetBuffer.writeVarInt(packetId)
        writer(packetBuffer)

        val packetBytes = byteOut.toByteArray()

        val finalOut = ByteArrayOutputStream()
        VarInt.write(finalOut, packetBytes.size)
        finalOut.write(packetBytes)

        withContext(Dispatchers.IO) {
            output.write(finalOut.toByteArray())
            output.flush()
        }
    }

    suspend fun disconnect() {
        if (!alive.compareAndSet(true, false)) return
        try { socket.close() } catch (_: Exception) {}

        onDisconnect(this)
    }

    fun setProtocolState(state: ProtocolState) {
        this.protocolState = state
    }
    fun getScope(): CoroutineScope = scope
}