package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

object GenesisRecipeEvents {
    val ALLOW = EventFactory.createArrayBacked(Allow::class.java) { listeners ->
        Allow { player, input, result ->
            for (listener in listeners) {
                val result = listener.allow(player, input, result)
                if (!result) return@Allow false
            }
            true
        }
    }

    fun interface Allow {
        fun allow(player: ServerPlayer?, input: Array<ItemStack>, result: ItemStack): Boolean
    }
}
