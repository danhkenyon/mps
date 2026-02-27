package net.minepact.mps.registry.core

class RegistryManager {
    private val registries = mutableMapOf<String, Registry<*>>()
    private var frozen = false

    fun <T> create(name: String): Registry<T> {
        check(!frozen) {
            "RegistryManager is frozen!"
        }
        val registry = Registry<T>(name)
        registries[name] = registry
        return registry
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String): Registry<T> {
        return registries[name] as? Registry<T> ?: error("Registry $name not found")
    }
    fun freeze() {
        registries.values.forEach { it.freeze() }
        frozen = true
    }
}