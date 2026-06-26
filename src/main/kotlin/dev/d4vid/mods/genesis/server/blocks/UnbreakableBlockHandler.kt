package dev.d4vid.mods.genesis.server.blocks

import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback
import dev.d4vid.mods.genesis.server.config.data.BlocksConfig
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.world.level.GameType

class UnbreakableBlockHandler {
    private lateinit var config: BlocksConfig

    init {
        GenesisConfigLoadCallback.EVENT.register { config = it.blocks }

        PlayerBlockBreakEvents.BEFORE.register { _, player, _, state, _ ->
            player.gameMode() == GameType.CREATIVE || !config.isUnbreakable(state)
        }
    }
}
