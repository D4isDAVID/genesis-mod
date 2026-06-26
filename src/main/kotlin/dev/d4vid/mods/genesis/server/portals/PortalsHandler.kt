package dev.d4vid.mods.genesis.server.portals

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.PortalsConfig
import dev.d4vid.mods.genesis.server.event.GenesisPortalEvents
import net.minecraft.world.level.Level

class PortalsHandler {
    private lateinit var config: PortalsConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.portals }

        GenesisPortalEvents.ALLOW_NETHER.register { level ->
            level == Level.NETHER || config.allowNether
        }

        GenesisPortalEvents.ALLOW_END.register { level ->
            level == Level.END || config.allowEnd
        }
    }
}
