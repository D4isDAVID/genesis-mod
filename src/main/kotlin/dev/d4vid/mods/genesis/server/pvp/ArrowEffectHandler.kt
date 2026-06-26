package dev.d4vid.mods.genesis.server.pvp

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.pvp.PvpConfig
import dev.d4vid.mods.genesis.server.event.GenesisCombatEvents

class ArrowEffectHandler {
    private lateinit var config: PvpConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.pvp }

        GenesisCombatEvents.ALLOW_ARROW_POTION.register {
            !config.isArrowPotionDisabled(it)
        }
    }
}
