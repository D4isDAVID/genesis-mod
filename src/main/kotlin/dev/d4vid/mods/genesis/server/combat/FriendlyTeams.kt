package dev.d4vid.mods.genesis.server.combat

import dev.d4vid.mods.genesis.server.GenesisConfig
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player

fun registerFriendlyTeamManager() {
    AttackEntityCallback.EVENT.register { player, _, _, entity, _ ->
        if (entity !is Player) {
            return@register InteractionResult.PASS
        }

        if (GenesisConfig.isCombatSpawnProtectionEnabled()) {
            val radius = GenesisConfig.getCombatSpawnProtectionRadius()
            val dx = entity.x - GenesisConfig.getCombatSpawnProtectionX()
            val dz = entity.z - GenesisConfig.getCombatSpawnProtectionZ()

            if ((dx * dx + dz * dz) <= (radius * radius)) {
                (player as ServerPlayer).sendSystemMessage(
                    Component.literal("This player is spawn protected.").withStyle(
                        ChatFormatting.RED
                    ), true
                )

                return@register InteractionResult.FAIL
            }
        }

        val teams = arrayOf(player.team, entity.team)

        if (teams.any { it != null && GenesisConfig.isTeamFriendly(it) }) {
            InteractionResult.FAIL
        } else {
            InteractionResult.PASS
        }
    }
}
