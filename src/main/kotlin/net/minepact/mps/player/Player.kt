package net.minepact.mps.player

import java.util.UUID

data class Player(
    val uuid: UUID,
    val username: String,
    val entityId: Int
)