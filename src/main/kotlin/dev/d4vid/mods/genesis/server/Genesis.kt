package dev.d4vid.mods.genesis.server

import dev.d4vid.mods.genesis.server.blocks.initializeUnbreakableBlockHandler
import dev.d4vid.mods.genesis.server.command.genesisCommand
import dev.d4vid.mods.genesis.server.command.registerCommand
import dev.d4vid.mods.genesis.server.config.GenesisConfig
import dev.d4vid.mods.genesis.server.cooldowns.ItemCooldownHandler
import dev.d4vid.mods.genesis.server.cooldowns.LungeCooldownHandler
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems
import dev.d4vid.mods.genesis.server.items.ItemLimitHandler
import dev.d4vid.mods.genesis.server.items.initializeDisabledItemsHandler
import dev.d4vid.mods.genesis.server.portals.initializePortalsHandler
import dev.d4vid.mods.genesis.server.pvp.CombatDetectionHandler
import dev.d4vid.mods.genesis.server.pvp.CombatProtectionHandler
import dev.d4vid.mods.genesis.server.pvp.initializeArrowEffectHandler
import dev.d4vid.mods.genesis.server.pvp.initializeCombatDamageMultiplier
import dev.d4vid.mods.genesis.server.recipes.initializeDisabledRecipeHandler
import net.fabricmc.api.DedicatedServerModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object Genesis : DedicatedServerModInitializer {
    const val MOD_ID = "genesis"
    private val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    private val config = GenesisConfig()
    private val itemLimit = ItemLimitHandler(config)
    private val combatDetection = CombatDetectionHandler(config)
    private val combatProtection = CombatProtectionHandler(config)
    private val lungeCooldown = LungeCooldownHandler(config, combatDetection)
    private val itemCooldown = ItemCooldownHandler(config, combatDetection)

    override fun onInitializeServer() {
        config.initialize()
        registerCommand(genesisCommand(config, combatProtection))

        initializeDisabledItemsHandler(config, combatDetection)
        itemLimit.initialize()

        initializeUnbreakableBlockHandler(config)
        initializeDisabledRecipeHandler(config)
        initializePortalsHandler(config)

        lungeCooldown.initialize()
        itemCooldown.initialize()

        combatDetection.initialize()
        combatProtection.initialize()
        initializeCombatDamageMultiplier(config)
        initializeArrowEffectHandler(config)

        GenesisItems.initialize()
    }
}
