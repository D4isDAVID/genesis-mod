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

    val ON_PLAYER_HIT_PLAYER = EventFactory.createArrayBacked(OnPlayerHitPlayer::class.java) { listeners ->
        OnPlayerHitPlayer { attacker, victim, stack ->
            for (listener in listeners) {
                listener.onPlayerHitPlayer(attacker, victim, stack)
            }
        }
    }

    val JUMP_INPUT = EventFactory.createArrayBacked(JumpInput::class.java) { listeners ->
        JumpInput { player ->
            for (listener in listeners) {
                listener.onJumpInput(player)
            }
        }
    }

    fun interface JumpInput {
        fun onJumpInput(player: ServerPlayer)
    }

    fun interface OnPlayerHitPlayer {
        fun onPlayerHitPlayer(attacker: ServerPlayer, victim: ServerPlayer, stack: ItemStack)
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
