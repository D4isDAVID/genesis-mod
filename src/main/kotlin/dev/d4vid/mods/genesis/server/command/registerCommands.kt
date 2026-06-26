package dev.d4vid.mods.genesis.server.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack

fun registerCommands(vararg commands: LiteralArgumentBuilder<CommandSourceStack>) {
    CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
        for (command in commands) {
            dispatcher.register(command)
        }
    }
}
