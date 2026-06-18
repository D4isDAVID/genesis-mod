package dev.d4vid.mods.genesis.server

import dev.d4vid.mods.genesis.server.combat.manageFriendlyTeams
import net.fabricmc.api.DedicatedServerModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object Genesis : DedicatedServerModInitializer {
    const val MOD_ID = "genesis"
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitializeServer() {
        registerCommand()
        manageFriendlyTeams()

        GenesisConfig.loadFile()
    }
}
