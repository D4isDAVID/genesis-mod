package dev.d4vid.mods.genesis.server.recipes

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class RecipeUnlockHandler {
    init {
        ServerPlayConnectionEvents.JOIN.register { handler, _, server ->
            handler.player.awardRecipes(server.recipeManager.recipes)
        }
    }
}
