package net.minepact.mps.registry.core

data class RegistryEntry<T>(
    val id: Int,
    val key: RegistryKey,
    val value: T
)