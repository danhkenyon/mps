package net.minepact.mps.core.version

object VersionRegistry {
    private lateinit var currentVersion: MinecraftVersion
    private val supportedProtocols = mutableSetOf<Int>()
    private var frozen = false

    fun register(version: MinecraftVersion) {
        check(!frozen) {
            "Cannot register versions after freeze()"
        }

        supportedProtocols += version.protocolVersion
        currentVersion = version
    }
    fun freeze() {
        frozen = true
    }

    fun current(): MinecraftVersion {
        check(::currentVersion.isInitialized) {
            "No MinecraftVersion registered"
        }
        return currentVersion
    }
    fun isProtocolSupported(protocol: Int): Boolean {
        return supportedProtocols.contains(protocol)
    }
}