package dev.d4vid.mods.genesis.server.cooldown

import dev.d4vid.mods.genesis.server.GenesisConfig
import dev.d4vid.mods.genesis.server.genesisComponent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import java.time.Duration
import java.time.Instant
import java.util.UUID

private fun cooldownComponent(type: CooldownType, duration: Duration): Component {
    return genesisComponent(
        Component.literal("Cooldowns")
            .withStyle(ChatFormatting.YELLOW),
        Component.empty()
            .append(Component.literal(type.toString())
                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD))
            .append(Component.literal(" cooldown for "))
            .append(Component.literal(duration.toString())
                .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD))
    )
}

class CooldownManager(val type: CooldownType = CooldownType.Lunge) {
    private val cooldowns = mutableMapOf<UUID, Instant>()

    fun apply(player: ServerPlayer): Boolean {
        val cooldown = cooldowns[player.uuid]
        val now = Instant.now()

        if (cooldown == null || cooldown.isBefore(now)) {
            val duration = GenesisConfig.getCooldownDuration(type)
            cooldowns[player.uuid] = now.plus(duration)

            player.sendSystemMessage(cooldownComponent(type, duration))

            return false
        }

        return true
    }
}
