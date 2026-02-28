package net.minepact.mps.protocol

enum class ProtocolState {
    HANDSHAKE,
    STATUS,
    LOGIN,
    CONFIGURATION,
    PLAY
}