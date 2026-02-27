package net.minepact.mps.core.version.v1_20_4

import net.minepact.mps.core.version.VersionProtocol
import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.play.JoinGamePacket
import net.minepact.mps.protocol.play.PlayerPositionAndLookPacket

class Protocol_1_20_4 : VersionProtocol {
    override suspend fun handleInitialPlay(connection: Connection) {

        val player = connection.player
            ?: error("Player not attached to connection")

        val world = connection.getServer().worldManager.getDefaultWorld()

        JoinGamePacket(
            entityId = player.entityId,
            isHardcore = false,
            gameMode = 1,
            dimensionName = world.dimension.name,
            hashedSeed = world.seed,
            spawnX = world.spawnPoint.x,
            spawnY = world.spawnPoint.y,
            spawnZ = world.spawnPoint.z
        ).handle(connection)

        PlayerPositionAndLookPacket(
            x = world.spawnPoint.x,
            y = world.spawnPoint.y,
            z = world.spawnPoint.z,
            yaw = world.spawnPoint.yaw,
            pitch = world.spawnPoint.pitch
        ).handle(connection)
    }
}