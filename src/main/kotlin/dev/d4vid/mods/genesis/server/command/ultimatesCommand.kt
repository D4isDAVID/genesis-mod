package dev.d4vid.mods.genesis.server.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.d4vid.mods.genesis.server.custom.item.DrillItem
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems
import dev.d4vid.mods.genesis.server.custom.item.MegaDrillItem
import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager
import dev.d4vid.mods.genesis.server.custom.item.util.UltimatePlayerData
import dev.d4vid.mods.genesis.server.recipes.UltimateCraftingHandler
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtAccounter
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.BundleContents
import net.minecraft.world.item.component.ItemContainerContents
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
        .then(
            Commands.literal("cancraft")
                .requires { source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER) }
                .then(
                    Commands.argument("enabled", BoolArgumentType.bool())
                        .executes { context ->
                            val enabled = BoolArgumentType.getBool(context, "enabled")
                            UltimateCraftingHandler.enabled = enabled

                            context.source.sendSuccess(
                                { Component.literal("Ultimate crafting is now ${if (enabled) "ENABLED" else "DISABLED"}") },
                                true,
                            )

                            Command.SINGLE_SUCCESS
                        }
                )
        )
        .then(
            Commands.literal("allowcraft")
                .requires { source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER) }
                .then(
                    Commands.argument("target", EntityArgument.players())
                        .then(
                            Commands.argument("allowed", BoolArgumentType.bool())
                                .executes { context ->
                                    val targets = EntityArgument.getPlayers(context, "target")
                                    val allowed = BoolArgumentType.getBool(context, "allowed")
                                    for (target in targets) {
                                        UltimatePlayerData.get(context.source.server)
                                            .setCraftingAllowed(target.uuid, allowed)
                                        context.source.sendSuccess(
                                            { Component.literal("${target.name.string} crafting override set to $allowed") },
                                            true
                                        )
                                    }
                                    Command.SINGLE_SUCCESS
                                }
                        )
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

private fun isKeeper(stack: ItemStack): Boolean {
    val item = GenesisItems.get(stack)
    return item is DrillItem || item is MegaDrillItem
}

private fun purgeStack(stack: ItemStack, removed: IntArray): ItemStack {
    if (stack.isEmpty) return stack

    if (UltimateManager.isUltimate(stack) && !isKeeper(stack)) {
        removed[0]++
        return ItemStack.EMPTY
    }

    val containerContents = stack.get(DataComponents.CONTAINER)
    if (containerContents != null) {
        val before = removed[0]
        val newItems = containerContents.stream().map { purgeStack(it, removed) }.toList()
        if (removed[0] != before) {
            stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(newItems))
        }
    }

    val bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS)
    if (bundleContents != null) {
        val before = removed[0]
        val newItems = bundleContents.itemsCopy().map { purgeStack(it, removed) }.filterNot { it.isEmpty }
        if (removed[0] != before) {
            stack.set(DataComponents.BUNDLE_CONTENTS, BundleContents(newItems))
        }
    }

    return stack
}

private fun purgeUltimates(context: CommandContext<CommandSourceStack>) {
    val server = context.source.server
    val registries = server.registryAccess()
    val ops = registries.createSerializationContext(NbtOps.INSTANCE)
    val removed = intArrayOf(0)

    fun cleanContainer(container: Container) {
        for (i in 0 until container.containerSize) {
            val stack = container.getItem(i)
            if (stack.isEmpty) continue
            container.setItem(i, purgeStack(stack, removed))
        }
    }

    val onlineIds = server.playerList.players.map { it.uuid }.toSet()
    for (online in server.playerList.players) {
        cleanContainer(online.inventory)
        cleanContainer(online.enderChestInventory)
    }

    val playerDataDir = server.getWorldPath(LevelResource.PLAYER_DATA_DIR)
    var failures = 0

    Files.list(playerDataDir).use { files ->
        files.filter { it.toString().endsWith(".dat") }.forEach { file ->
            val uuid = runCatching {
                UUID.fromString(file.fileName.toString().removeSuffix(".dat"))
            }.getOrNull() ?: return@forEach

            if (uuid in onlineIds) return@forEach

            runCatching {
                val root = NbtIo.readCompressed(file, NbtAccounter.unlimitedHeap())
                var changed = false

                for (key in listOf("Inventory", "EnderItems")) {
                    val list = root.getListOrEmpty(key)

                    for (i in 0 until list.size) {
                        val itemTag = list.getCompoundOrEmpty(i)
                        val decoded = ItemStack.OPTIONAL_CODEC.parse(ops, itemTag).result().orElse(ItemStack.EMPTY)
                        if (decoded.isEmpty) continue

                        val before = removed[0]
                        val result = purgeStack(decoded, removed)
                        if (removed[0] == before) continue

                        changed = true

                        val newTag = CompoundTag()
                        val slotTag = itemTag.get("Slot")
                        if (slotTag != null) newTag.put("Slot", slotTag)

                        if (!result.isEmpty) {
                            val encoded = ItemStack.CODEC.encodeStart(ops, result).result().orElse(null)
                            if (encoded is CompoundTag) newTag.merge(encoded)
                        }

                        list.set(i, newTag)
                    }
                }

                if (changed) {
                    NbtIo.writeCompressed(root, file)
                }
            }.onFailure {
                failures++
                context.source.sendFailure(Component.literal("Failed to process ${file.fileName}: ${it.message}"))
            }
        }
    }

    context.source.sendSuccess(
        { Component.literal("Purged ${removed[0]} non-Drill ultimate(s) from all player data" +
            if (failures > 0) " ($failures file(s) failed — see above)" else "") },
        true,
    )
}
