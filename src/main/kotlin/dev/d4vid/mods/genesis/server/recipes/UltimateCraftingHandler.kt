package dev.d4vid.mods.genesis.server.recipes

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.RecipesConfig
import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager
import dev.d4vid.mods.genesis.server.event.GenesisRecipeEvents

class UltimateCraftingHandler {
    private lateinit var config: RecipesConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.recipes }

        GenesisRecipeEvents.ALLOW.register { _, _, result ->
            !UltimateManager.isUltimate(result) || config.ultimateCraftingEnabled
        }
    }
}
