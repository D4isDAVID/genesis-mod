package dev.d4vid.mods.genesis.server.recipes

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.RecipesConfig
import dev.d4vid.mods.genesis.server.event.GenesisRecipeEvents

class DisabledRecipeHandler {
    private lateinit var config: RecipesConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.recipes }

        GenesisRecipeEvents.ALLOW.register { player, input, result ->
            !config.isResultDisabled(result) && input.none {
                config.isIngredientDisabled(it)
            }
        }
    }
}
