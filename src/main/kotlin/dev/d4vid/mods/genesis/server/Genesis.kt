package dev.d4vid.mods.genesis.server

import dev.d4vid.mods.genesis.server.blocks.UnbreakableBlockHandler
import dev.d4vid.mods.genesis.server.chat.ChatHandler
import dev.d4vid.mods.genesis.server.command.bullshitCommand
import dev.d4vid.mods.genesis.server.command.genesisCommand
import dev.d4vid.mods.genesis.server.command.registerCommands
import dev.d4vid.mods.genesis.server.config.GenesisConfig
import dev.d4vid.mods.genesis.server.cooldowns.ItemCooldownHandler
import dev.d4vid.mods.genesis.server.cooldowns.LungeCooldownHandler
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems
import dev.d4vid.mods.genesis.server.items.DisabledItemsHandler
import dev.d4vid.mods.genesis.server.items.ItemLimitHandler
import dev.d4vid.mods.genesis.server.portals.PortalsHandler
import dev.d4vid.mods.genesis.server.pvp.ArrowEffectHandler
import dev.d4vid.mods.genesis.server.pvp.CombatDamageMultiplier
import dev.d4vid.mods.genesis.server.pvp.CombatDetectionHandler
import dev.d4vid.mods.genesis.server.pvp.CombatProtectionHandler
import dev.d4vid.mods.genesis.server.recipes.DisabledRecipeHandler
import dev.d4vid.mods.genesis.server.spoof.PacketSpoofHandler
import net.fabricmc.api.DedicatedServerModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object Genesis : DedicatedServerModInitializer {
    const val MOD_ID = "genesis"
    private val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitializeServer() {
        val config = GenesisConfig()

        PacketSpoofHandler()

        val combatDetection = CombatDetectionHandler()
        val combatProtection = CombatProtectionHandler()

        ChatHandler()

        DisabledItemsHandler(combatDetection)
        ItemLimitHandler()

        ItemCooldownHandler(combatDetection)
        LungeCooldownHandler(combatDetection)

        UnbreakableBlockHandler()
        DisabledRecipeHandler()
        PortalsHandler()

        CombatDamageMultiplier()
        ArrowEffectHandler()

        registerCommands(
            genesisCommand(config, combatProtection),
            bullshitCommand(),
        )

        GenesisItems.initialize()
    }
}
