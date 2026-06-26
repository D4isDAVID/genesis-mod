package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

object GenesisCustomItemEvents {
    val ALLOW_PLAYER_FALL_DAMAGE = EventFactory.createArrayBacked(AllowPlayerFallDamage::class.java) { listeners ->
        AllowPlayerFallDamage { player, fallDistance, multiplier, source ->
            for (listener in listeners) {
                val result = listener.allowPlayerFallDamage(player, fallDistance, multiplier, source)

                if (!result) {
                    return@AllowPlayerFallDamage false
                }
            }
            true
        }
    }

    val ALLOW_ITEM_SWAP = EventFactory.createArrayBacked(AllowItemSwap::class.java) { listeners ->
        AllowItemSwap { player, stack ->
            for (listener in listeners) {
                val result = listener.allowItemSwap(player, stack)

                if (!result) {
                    return@AllowItemSwap false
                }
            }
            true
        }
    }

    fun interface AllowPlayerFallDamage {
        fun allowPlayerFallDamage(
            player: ServerPlayer,
            fallDistance: Double,
            multiplier: Float,
            source: DamageSource
        ): Boolean
    }

    fun interface AllowItemSwap {
        fun allowItemSwap(player: ServerPlayer, stack: ItemStack): Boolean
    }
}
