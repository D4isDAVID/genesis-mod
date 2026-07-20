package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

object GenesisCustomItemEvents {
    val ALLOW_ENTITY_FALL_DAMAGE = EventFactory.createArrayBacked(AllowEntityFallDamage::class.java) { listeners ->
        AllowEntityFallDamage { entity, fallDistance, multiplier, source ->
            for (listener in listeners) {
                val result = listener.allowEntityFallDamage(entity, fallDistance, multiplier, source)

                if (!result) {
                    return@AllowEntityFallDamage false
                }
            }

            true
        }
    }

    val ALLOW_PLAYER_ACTION = EventFactory.createArrayBacked(AllowPlayerAction::class.java) { listeners ->
        AllowPlayerAction { player, packet ->
            for (listener in listeners) {
                val result = listener.allowPlayerAction(player, packet)

                if (!result) {
                    return@AllowPlayerAction false
                }
            }

            true
        }
    }

    val JUMP_INPUT = EventFactory.createArrayBacked(JumpInput::class.java) { listeners ->
        JumpInput { player ->
            for (listener in listeners) {
                listener.onJumpInput(player)
            }
        }
    }

    fun interface AllowEntityFallDamage {
        fun allowEntityFallDamage(
            entity: LivingEntity,
            fallDistance: Double,
            multiplier: Float,
            source: DamageSource
        ): Boolean
    }

    fun interface AllowPlayerAction {
        fun allowPlayerAction(player: ServerPlayer, packet: ServerboundPlayerActionPacket): Boolean
    }

    fun interface JumpInput {
        fun onJumpInput(player: ServerPlayer)
    }
}
