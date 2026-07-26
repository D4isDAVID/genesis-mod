package dev.d4vid.mods.genesis.server.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.d4vid.mods.genesis.server.custom.item.DrillItem
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems
import dev.d4vid.mods.genesis.server.custom.item.MegaDrillItem
import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtAccounter
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.LevelResource
import java.nio.file.Files
import java.util.UUID

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
        .then(
            Commands.literal("purge")
                .requires { source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER) }
                .executes { context ->
                    purgeUltimates(context)
                    Command.SINGLE_SUCCESS
                }
        )
}

private fun wipeUltimate(context: CommandContext<CommandSourceStack>, target: ServerPlayer) {
    UltimateManager.wipe(target)

    context.source.sendSuccess(
        { Component.literal("Wiped ${target.name.string}'s ultimate craft record") },
        true,
    )
}

private fun isKeeper(stack: ItemStack): Boolean {
    val item = GenesisItems.get(stack)
    return item is DrillItem || item is MegaDrillItem
}

private fun purgeUltimates(context: CommandContext<CommandSourceStack>) {
    val server = context.source.server
    val registries = server.registryAccess()
    var removed = 0

    fun cleanContainer(container: Container) {
        for (i in 0 until container.containerSize) {
            val stack = container.getItem(i)
            if (UltimateManager.isUltimate(stack) && !isKeeper(stack)) {
                container.setItem(i, ItemStack.EMPTY)
                removed++
            }
        }
    }

    val onlineIds = server.playerList.players.map { it.uuid }.toSet()
    for (online in server.playerList.players) {
        cleanContainer(online.inventory)
        cleanContainer(online.enderChestInventory)
    }

    val playerDataDir = server.getWorldPath(LevelResource.PLAYER_DATA_DIR)

    Files.list(playerDataDir).use { files ->
        files.filter { it.toString().endsWith(".dat") }.forEach { file ->
            val uuid = runCatching {
                UUID.fromString(file.fileName.toString().removeSuffix(".dat"))
            }.getOrNull() ?: return@forEach

            if (uuid in onlineIds) return@forEach

            val root = NbtIo.readCompressed(file, NbtAccounter.unlimitedHeap())
            var changed = false

            for (key in listOf("Inventory", "EnderItems")) {
                val list = root.getList(key).orElse(net.minecraft.nbt.ListTag())
                val toRemove = mutableListOf<Int>()

                for (i in 0 until list.size) {
                    val itemTag = list.getCompound(i).orElse(null) ?: continue
                    val stack = ItemStack.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, itemTag)
                        .result().orElse(ItemStack.EMPTY)

                    if (UltimateManager.isUltimate(stack) && !isKeeper(stack)) {
                        toRemove.add(i)
                        removed++
                        changed = true
                    }
                }

                for (i in toRemove.reversed()) {
                    list.removeAt(i)
                }

                root.put(key, list)
            }

            if (changed) {
                NbtIo.writeCompressed(root, file)
            }
        }
    }

    context.source.sendSuccess(
        { Component.literal("Purged $removed non-Drill ultimate(s) from all player data") },
        true,
    )
}
