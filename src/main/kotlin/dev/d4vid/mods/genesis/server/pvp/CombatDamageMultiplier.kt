package dev.d4vid.mods.genesis.server.pvp

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.pvp.PvpDamageMultipliersConfig
import dev.d4vid.mods.genesis.server.event.GenesisCombatEvents

class CombatDamageMultiplier {
    private lateinit var config: PvpDamageMultipliersConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.pvp.damageMultipliers }

        GenesisCombatEvents.MODIFY_MINECART_TNT_EXPLOSION_RADIUS.register {
            it * config.minecartTntExplosion
        }

        GenesisCombatEvents.MODIFY_RESPAWN_ANCHOR_EXPLOSION_RADIUS.register {
            it * config.respawnAnchorExplosion
        }

        GenesisCombatEvents.MODIFY_END_CRYSTAL_EXPLOSION_RADIUS.register {
            it * config.endCrystalExplosion
        }
    }
}
