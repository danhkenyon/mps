package net.minepact.mps.player

import net.minepact.mps.network.Connection
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class PlayerManager {
    private val playersByUUID = ConcurrentHashMap<UUID, Player>()
    private val playersByName = ConcurrentHashMap<String, Player>()
    private val entityIdCounter = AtomicInteger(1)

    fun createPlayer(
        uuid: UUID,
        username: String,
        connection: Connection
    ): Player {
        val entityId = entityIdCounter.getAndIncrement()
        val player = Player(uuid, username, connection, entityId)

        playersByUUID[uuid] = player
        playersByName[username.lowercase()] = player
        return player
    }
    fun remove(player: Player) {
        playersByUUID.remove(player.uuid)
        playersByName.remove(player.username.lowercase())
    }

    fun getByUUID(uuid: UUID): Player? = playersByUUID[uuid]
    fun getByName(name: String): Player? = playersByName[name.lowercase()]

    fun all(): Collection<Player> = playersByUUID.values
    fun onlineCount(): Int = playersByUUID.size
}