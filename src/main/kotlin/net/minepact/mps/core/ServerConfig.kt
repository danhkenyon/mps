package net.minepact.mps.core

data class ServerConfig(
    val host: String = "0.0.0.0",
    val port: Int = 25565,
    val onlineMode: Boolean = false,
    val maxPlayers: Int = 20,
    val motd: String = "MinePact Server"
)