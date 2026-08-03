package dev.d4vid.mods.genesis.server.items

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.items.ItemsConfig
import dev.d4vid.mods.genesis.server.event.GenesisItemEvents
import dev.d4vid.mods.genesis.server.pvp.CombatDetectionHandler
import net.fabricmc.fabric.api.event.player.ItemEvents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.ItemStack

class DisabledItemsHandler(private val combatDetection: CombatDetectionHandler) {
    private lateinit var config: ItemsConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.items }

        GenesisItemEvents.ALLOW_TOTEM.register { _ ->
            !config.disableTotemDeathProtection
        }

        ItemEvents.USE.register { _, player, hand ->
            val stack = player.getItemBySlot(hand.asEquipmentSlot())

            if (allowItemUsage(player as ServerPlayer, stack)) {
                null
            } else {
                InteractionResult.FAIL
            }
        }

        ItemEvents.USE_ON.register { context ->
            if (allowItemUsage(context.player as ServerPlayer, context.itemInHand)) {
                null
            } else {
                InteractionResult.FAIL
            }
        }
    }

    private fun allowItemUsage(
        player: ServerPlayer,
        stack: ItemStack
    ): Boolean {
        return !config.isItemUsageDisabled(stack, combatDetection.isPlayerInCombat(player))
    }
}
