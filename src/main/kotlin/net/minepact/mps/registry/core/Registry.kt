package net.minepact.mps.registry.core

class Registry<T>(
    val name: String
) {
    private val entries = mutableListOf<RegistryEntry<T>>()
    private val byKey = mutableMapOf<RegistryKey, RegistryEntry<T>>()
    private var frozen = false

    fun register(key: RegistryKey, value: T): RegistryEntry<T> {
        check(!frozen) {
            "Registry $name is frozen!"
        }

        val entry = RegistryEntry(entries.size, key, value)
        entries += entry
        byKey[key] = entry
        return entry
    }

    fun get(key: RegistryKey): RegistryEntry<T>? = byKey[key]
    fun entries(): List<RegistryEntry<T>> = entries
    fun freeze() {
        frozen = true
    }
}