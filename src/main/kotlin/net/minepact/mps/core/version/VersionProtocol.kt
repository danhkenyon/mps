package net.minepact.mps.core.version

import net.minepact.mps.network.Connection

interface VersionProtocol {
    suspend fun handleConfiguration(connection: Connection)
    suspend fun handleInitialPlay(connection: Connection)
}