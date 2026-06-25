package dev.d4vid.mods.genesis.server.chat

import dev.d4vid.mods.genesis.server.config.data.chat.ChatConfig
import dev.d4vid.mods.genesis.server.config.field.ChatKeywordContent
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions

class ChatKeywordHandler(private val config: ChatConfig, private val player: ServerPlayer) {
    private val applied = mutableSetOf<ChatKeywordContent>()
    private val op = player.permissions().hasPermission(Permissions.COMMANDS_OWNER)

    fun apply(component: Component): Component {
        return component.applyKeywords()
    }

    private fun Component.applyKeywords(): Component {
        if (siblings.isNotEmpty()) {
            return applySiblingKeywords()
        }

        for (keyword in config.keywords) {
            if (keyword.requireOp && !op) {
                continue
            }

            val parts = string.split(keyword.base)
            val first = parts.first()
            val latter = parts.drop(1)

            if (latter.isEmpty()) {
                continue
            }

            return Component.empty()
                .append(Component.literal(first.removeSuffix("\\")).applyKeywords())
                .also {
                    if (first.endsWith('\\')) {
                        it.append(Component.literal(keyword.base))
                        return@also
                    }

                    if (applied.contains(keyword.content)) {
                        return@also
                    }

                    applied.add(keyword.content)
                    it.append(keyword.content.content(player) ?: Component.literal(keyword.base))
                }
                .append(Component.literal(latter.joinToString("")).applyKeywords())
                .withStyle(style)

        }

        return this
    }

    private fun Component.applySiblingKeywords(): Component {
        return siblings.fold(Component.empty().withStyle(style)) { new, sibling ->
            new.append(sibling.applyKeywords())
        }
    }
}
