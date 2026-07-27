package dev.d4vid.mods.genesis.server.recipes

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager
import dev.d4vid.mods.genesis.server.event.GenesisRecipeEvents

class UltimateCraftingHandler {
    companion object {
        @Volatile
        var enabled: Boolean = false
    }

    init {
        GenesisConfigLoadCallback.EVENT.register { enabled = it.recipes.ultimateCraftingEnabled }

        GenesisRecipeEvents.ALLOW.register { _, _, result ->
            !UltimateManager.isUltimate(result) || enabled
        }
    }
}
