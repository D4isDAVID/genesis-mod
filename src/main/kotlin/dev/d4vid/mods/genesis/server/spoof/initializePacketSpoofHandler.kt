package dev.d4vid.mods.genesis.server.spoof

import dev.d4vid.mods.genesis.server.config.GenesisConfig
import dev.d4vid.mods.genesis.server.event.GenesisPacketSpoofEvents

fun initializePacketSpoofHandler(config: GenesisConfig) {
    GenesisPacketSpoofEvents.ALLOW_ENTITY_HEALTH_SYNC.register {
        !config.data.spoofClientPackets.disableEntityHealthUpdates
    }
}
