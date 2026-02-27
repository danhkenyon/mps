package net.minepact.mps.protocol.login

import net.minepact.mps.core.MinePactServer
import net.minepact.mps.network.Connection
import net.minepact.mps.protocol.ProtocolState
import net.minepact.mps.player.Player
import net.minepact.mps.protocol.play.JoinGamePacket
import net.minepact.mps.protocol.play.KeepAlivePacket
import net.minepact.mps.protocol.play.PlayerPositionAndLookPacket
import java.util.*

class LoginStartManager(
    private val server: MinePactServer
) {

    suspend fun completeLogin(connection: Connection, username: String) {
        val server = connection.getServer()
        val world = server.worldManager.getDefaultWorld()

        val player = server.getPlayerManager().createPlayer(
            uuid = UUID.randomUUID(),
            username = username,
            connection = connection
        )

        connection.player = player
        connection.setProtocolState(ProtocolState.PLAY)

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

        KeepAlivePacket().handle(connection)
    }
}