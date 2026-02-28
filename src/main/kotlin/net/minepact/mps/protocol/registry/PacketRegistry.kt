package net.minepact.mps.protocol.registry

import net.minepact.mps.protocol.Packet
import net.minepact.mps.protocol.ProtocolState
import net.minepact.mps.protocol.configuration.AcknowledgeFinishConfigurationPacket
import net.minepact.mps.protocol.configuration.ServerboundKnownPacksPacket
import net.minepact.mps.protocol.handshake.HandshakePacket
import net.minepact.mps.protocol.login.LoginAcknowledgedPacket
import net.minepact.mps.protocol.login.LoginStartPacket
import net.minepact.mps.protocol.play.ConfirmTeleportPacket
import net.minepact.mps.protocol.play.KeepAliveServerboundPacket
import net.minepact.mps.protocol.play.PlayerPositionAndRotationPacket
import net.minepact.mps.protocol.play.PlayerPositionPacket
import net.minepact.mps.protocol.play.PlayerRotationPacket
import net.minepact.mps.protocol.status.*

object PacketRegistry {

    private val registry = mutableMapOf<ProtocolState, MutableMap<Int, () -> Packet>>()

    init {
        // Handshake
        register(ProtocolState.HANDSHAKE, 0x00) { HandshakePacket() }

        // Status
        register(ProtocolState.STATUS, 0x00) { StatusRequestPacket() }
        register(ProtocolState.STATUS, 0x01) { StatusPingPacket() }

        // Login
        register(ProtocolState.LOGIN, 0x00) { LoginStartPacket() }
        register(ProtocolState.LOGIN, 0x03) { LoginAcknowledgedPacket() }

        // Configuration
        register(ProtocolState.CONFIGURATION, 0x02) { AcknowledgeFinishConfigurationPacket() }
        register(ProtocolState.CONFIGURATION, 0x07) { ServerboundKnownPacksPacket() }

        // Play
        register(ProtocolState.PLAY, 0x00) { ConfirmTeleportPacket() }
        register(ProtocolState.PLAY, 0x12) { KeepAliveServerboundPacket() }
        register(ProtocolState.PLAY, 0x14) { PlayerPositionPacket() }
        register(ProtocolState.PLAY, 0x15) { PlayerPositionAndRotationPacket() }
        register(ProtocolState.PLAY, 0x16) { PlayerRotationPacket() }
    }

    fun register(state: ProtocolState, id: Int, supplier: () -> Packet) {
        registry.computeIfAbsent(state) { mutableMapOf() }[id] = supplier
    }

    fun create(state: ProtocolState, id: Int): Packet? =
        registry[state]?.get(id)?.invoke()
}