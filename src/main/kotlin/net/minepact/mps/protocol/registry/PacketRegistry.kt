package net.minepact.mps.protocol.registry

import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.ProtocolState
import net.minepact.mps.protocol.handshake.HandshakePacket
import net.minepact.mps.protocol.login.LoginStartPacket
import net.minepact.mps.protocol.status.*

object PacketRegistry {

    private val registry =
        mutableMapOf<ProtocolState, MutableMap<Int, () -> Packet>>()

    init {
        register(ProtocolState.HANDSHAKE, 0x00) { HandshakePacket() }

        register(ProtocolState.STATUS, 0x00) { StatusRequestPacket() }
        register(ProtocolState.STATUS, 0x01) { StatusPingPacket() }

        register(ProtocolState.LOGIN, 0x00) { LoginStartPacket() }
    }

    fun register(
        state: ProtocolState,
        id: Int,
        supplier: () -> Packet
    ) {
        registry.computeIfAbsent(state) { mutableMapOf() }[id] = supplier
    }

    fun create(
        state: ProtocolState,
        id: Int
    ): Packet? {
        return registry[state]?.get(id)?.invoke()
    }
}