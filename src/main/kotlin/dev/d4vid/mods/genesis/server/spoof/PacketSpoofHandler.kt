package dev.d4vid.mods.genesis.server.spoof

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.SpoofClientPacketsConfig
import dev.d4vid.mods.genesis.server.event.GenesisPacketSpoofEvents
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.entity.boss.wither.WitherBoss

class PacketSpoofHandler {
    private lateinit var config: SpoofClientPacketsConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.spoofClientPackets }

        GenesisPacketSpoofEvents.ALLOW_ENTITY_HEALTH_SYNC.register {
            it is EnderDragon || it is WitherBoss || !config.disableEntityHealthUpdates
        }
    }
}
