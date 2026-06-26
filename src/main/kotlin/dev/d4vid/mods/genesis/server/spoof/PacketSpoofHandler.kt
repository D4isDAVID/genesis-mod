package dev.d4vid.mods.genesis.server.spoof

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.SpoofClientPacketsConfig
import dev.d4vid.mods.genesis.server.event.GenesisPacketSpoofEvents

class PacketSpoofHandler {
    private lateinit var config: SpoofClientPacketsConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.spoofClientPackets }

        GenesisPacketSpoofEvents.ALLOW_ENTITY_HEALTH_SYNC.register {
            !config.disableEntityHealthUpdates
        }
    }
}
