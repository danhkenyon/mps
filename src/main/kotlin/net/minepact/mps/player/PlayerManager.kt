package net.minepact.mps.player

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class PlayerManager {
    private val byUUID = ConcurrentHashMap<UUID, Player>()
    private val byName = ConcurrentHashMap<String, Player>()
    private val entityIdCounter = AtomicInteger(1)

    fun createPlayer(uuid: UUID, username: String): Player {
        val player = Player(uuid, username, entityIdCounter.getAndIncrement())
        byUUID[uuid] = player
        byName[username.lowercase()] = player
        return player
    }
    fun remove(player: Player) {
        byUUID.remove(player.uuid)
        byName.remove(player.username.lowercase())
    }

    fun getByUUID(uuid: UUID): Player? = byUUID[uuid]
    fun getByName(name: String): Player? = byName[name.lowercase()]
    fun all(): Collection<Player> = byUUID.values
    fun onlineCount(): Int = byUUID.size
}