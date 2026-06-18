package dev.d4vid.mods.genesis.server

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.permissions.Permissions

fun registerCommand() {
    CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
        dispatcher.register(command())
    }
}

private fun command(): LiteralArgumentBuilder<CommandSourceStack> {
    return Commands.literal("genesis")
        .requires { source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER) }
        .then(reloadCommand())
}

private fun reloadCommand(): LiteralArgumentBuilder<CommandSourceStack> {
    return Commands.literal("reload")
        .requires { source -> !source.isPlayer }
        .executes { _ ->
            GenesisConfig.loadFile()

            0
        }
}
