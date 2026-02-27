package net.minepact.mps.core.version

data class MinecraftVersion(
    val name: String,
    val protocolVersion: Int,
    val dataVersion: Int,
    val protocol: VersionProtocol
)