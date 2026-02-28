package net.minepact.mps.core.version.v1_20_4

import net.minepact.mps.core.version.VersionProtocol
import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.configuration.BiomeRegistryDataPacket
import net.minepact.mps.protocol.configuration.ClientboundKnownPacksPacket
import net.minepact.mps.protocol.configuration.DimensionRegistryDataPacket
import net.minepact.mps.protocol.configuration.FeatureFlagsPacket
import net.minepact.mps.protocol.configuration.FinishConfigurationPacket
import net.minepact.mps.protocol.play.ChunkDataAndUpdateLightPacket
import net.minepact.mps.protocol.play.GameEventPacket
import net.minepact.mps.protocol.play.JoinGamePacket
import net.minepact.mps.protocol.play.PlayerAbilitiesPacket
import net.minepact.mps.protocol.play.PlayerPositionAndLookPacket
import net.minepact.mps.protocol.play.SetDefaultSpawnPositionPacket

class Protocol_1_20_4 : VersionProtocol {
    override suspend fun handleConfiguration(connection: Connection) {
        ClientboundKnownPacksPacket().handle(connection)
        FeatureFlagsPacket().handle(connection)
        DimensionRegistryDataPacket().handle(connection)
        BiomeRegistryDataPacket().handle(connection)
        FinishConfigurationPacket().handle(connection)
    }

    override suspend fun handleInitialPlay(connection: Connection) {
        val player = connection.player
            ?: error("Player not attached to connection during handleInitialPlay")

        val world = connection.getServer().worldManager.getDefaultWorld()

        JoinGamePacket(
            entityId      = player.entityId,
            isHardcore    = false,
            gameMode      = 1,
            dimensionName = world.dimensionName,
            hashedSeed    = world.seed,
            spawnX        = world.spawnX,
            spawnY        = world.spawnY,
            spawnZ        = world.spawnZ
        ).handle(connection)
        PlayerAbilitiesPacket.creative().handle(connection)
        SetDefaultSpawnPositionPacket(
            x = world.spawnX.toInt(),
            y = world.spawnY.toInt(),
            z = world.spawnZ.toInt()
        ).handle(connection)
        GameEventPacket.startWaitingForChunks().handle(connection)

        val cx = Math.floorDiv(world.spawnX.toInt(), 16)
        val cz = Math.floorDiv(world.spawnZ.toInt(), 16)
        for (dx in -2..2) {
            for (dz in -2..2) {
                ChunkDataAndUpdateLightPacket(cx + dx, cz + dz).handle(connection)
            }
        }

        PlayerPositionAndLookPacket(
            x = world.spawnX,
            y = world.spawnY,
            z = world.spawnZ,
            yaw = world.spawnYaw,
            pitch = world.spawnPitch
        ).handle(connection)
    }
}