package dev.d4vid.mods.genesis.server.recipes

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.RecipesConfig
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems
import dev.d4vid.mods.genesis.server.event.GenesisRecipeEvents

class DisabledRecipeHandler {
    private lateinit var config: RecipesConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.recipes }

        GenesisRecipeEvents.ALLOW.register { _, input, result ->
            val resultAllowed = GenesisItems.`is`(result) || !config.isResultDisabled(result)
            val ingredientsAllowed = input.all { GenesisItems.`is`(it) || !config.isIngredientDisabled(it) }
            resultAllowed && ingredientsAllowed
        }
    }
}
