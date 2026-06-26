package dev.d4vid.mods.genesis.server.cooldowns

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.cooldowns.CooldownsCustomConfig
import dev.d4vid.mods.genesis.server.event.GenesisCooldownEvents
import dev.d4vid.mods.genesis.server.pvp.CombatDetectionHandler
import net.minecraft.tags.ItemTags
import java.util.*
import kotlin.time.Clock
import kotlin.time.Instant

class LungeCooldownHandler(private val combatDetection: CombatDetectionHandler) {
    private lateinit var config: CooldownsCustomConfig
    private val cooldowns = mutableMapOf<UUID, Instant>()

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.cooldowns.lunge }

        GenesisCooldownEvents.ALLOW_LUNGE.register { level, _, itemInUse, player, _ ->
            val stack = itemInUse.itemStack

            if (!stack.`is`(ItemTags.SPEARS)) {
                return@register true
            }

            if (cooldowns[player.uuid]?.let { it > Clock.System.now() } == true) {
                return@register false
            }

            val cooldown = if (combatDetection.isPlayerInCombat(player)) {
                config.inCombat
            } else {
                config.global
            }

            cooldowns[player.uuid] = Clock.System.now().plus(cooldown)

            val tickRate = level.server.tickRateManager().tickrate()
            player.cooldowns.addCooldown(stack, (cooldown.inWholeSeconds * tickRate).toInt())

            true
        }

        GenesisCooldownEvents.ALLOW_ITEM_COOLDOWN.register { stack ->
            !stack.`is`(ItemTags.SPEARS)
        }
    }
}
