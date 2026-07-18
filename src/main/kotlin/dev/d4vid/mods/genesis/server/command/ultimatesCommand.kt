package dev.d4vid.mods.genesis.server.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions

fun ultimatesCommand(): LiteralArgumentBuilder<CommandSourceStack> {
    return Commands.literal("ultimates")
        .then(
            Commands.literal("wipe")
                .requires { source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER) }
                .then(
                    Commands.argument("target", EntityArgument.players())
                        .executes { context ->
                            val targets = EntityArgument.getPlayers(context, "target")

                            for (target in targets) {
                                wipeUltimate(context, target)
                            }

                            Command.SINGLE_SUCCESS
                        }
                )
        )
}

private fun wipeUltimate(context: CommandContext<CommandSourceStack>, target: ServerPlayer) {
    UltimateManager.wipe(target)

    context.source.sendSuccess(
        { Component.literal("Wiped ${target.name.string}'s ultimate craft record") },
        true,
    )
}
