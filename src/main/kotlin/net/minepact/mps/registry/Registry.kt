package net.minepact.mps.registry

class Registry<T>(
    val key: String
) {
    private val entries = mutableListOf<RegistryEntry<T>>()

    fun register(name: String, element: T): RegistryEntry<T> {
        val entry = RegistryEntry(name, entries.size, element)
        entries.add(entry)
        return entry
    }
    fun getEntries(): List<RegistryEntry<T>> = entries
}