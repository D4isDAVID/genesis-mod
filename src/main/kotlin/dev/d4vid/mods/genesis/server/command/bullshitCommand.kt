package dev.d4vid.mods.genesis.server.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.d4vid.mods.genesis.server.custom.item.bullshit.BullshitItems
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.permissions.Permissions

fun bullshitCommand(): LiteralArgumentBuilder<CommandSourceStack> {
    val command = Commands.literal("bullshit")
        .requires { source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER) }

    BullshitItems.REGISTRY.forEach { (_, item) ->
        command.then(
            Commands.literal("give")
                .then(
                    Commands.literal(item.id.path)
                        .executes { ctx ->
                            val player = ctx.source.playerOrException
                            player.inventory.add(item.assemble(player.level().registryAccess()))
                            0
                        }
                        .then(
                            Commands.argument("target", EntityArgument.player())
                                .executes { ctx ->
                                    val target = EntityArgument.getPlayer(ctx, "target")
                                    target.inventory.add(item.assemble(target.level().registryAccess()))
                                    0
                                }
                        )
                )
        )
    }

    return command
}
