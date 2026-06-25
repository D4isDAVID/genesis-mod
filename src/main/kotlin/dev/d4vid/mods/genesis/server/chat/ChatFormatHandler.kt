package dev.d4vid.mods.genesis.server.chat

import dev.d4vid.mods.genesis.server.config.data.chat.ChatConfig
import dev.d4vid.mods.genesis.server.config.data.chat.ChatFormatConfig
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import java.net.URI

class ChatFormatHandler(private val config: ChatConfig, private val player: ServerPlayer) {
    private val server = player.level().server
    private val op = player.permissions().hasPermission(Permissions.COMMANDS_OWNER)

    fun apply(component: Component): Component {
        return Component.empty().append(component.applyFormats())
    }

    private fun Component.applyFormats(): Component {
        val (content, format) = findBestMatch(string) ?: return this
        val newStyle = if (content.first.endsWith('\\')) {
            Style.EMPTY
        } else {
            format.format.let {
                if (it.clickEvent is ClickEvent.OpenUrl) {
                    runCatching { URI(content.second).toURL() }
                        .map { url ->
                            it.withClickEvent(ClickEvent.OpenUrl(url.toURI()))
                                .withColor(ChatFormatting.BLUE)
                                .withUnderlined(true)
                        }
                        .getOrDefault(it.withClickEvent(ClickEvent.SuggestCommand("")))
                } else {
                    it
                }
            }.let {
                if (it.hoverEvent is HoverEvent.ShowText) {
                    it.withHoverEvent(
                        HoverEvent.ShowText(Component.literal(content.second).withStyle(it).applyFormats())
                    )
                } else {
                    it
                }
            }
        }

        if (format.replaceContent != null) {
            server.sendSystemMessage(
                Component.empty()
                    .append(player.name)
                    .append(" sent in spoiler: ")
                    .append(Component.literal(content.second))
            )
        }

        return Component.empty()
            .withStyle(style)
            .append(Component.literal(content.first.removeSuffix("\\")).applyFormats())
            .append(Component.literal(format.startReplace ?: "").withStyle(newStyle))
            .append(Component.literal(format.replaceContent ?: content.second).withStyle(newStyle).applyFormats())
            .append(Component.literal(format.endReplace ?: "").withStyle(newStyle))
            .also { new ->
                content.third?.let { new.append(Component.literal(it).applyFormats()) }
            }
    }

    private typealias FormatMatch = Pair<Triple<String, String, String?>, ChatFormatConfig>

    private fun findBestMatch(text: String): FormatMatch? {
        var best: FormatMatch? = null
        var bestPrefixLen = Int.MAX_VALUE

        for (format in config.formats) {
            if (format.requireOp && !op) {
                continue
            }

            val parts = format.start.split(text, 2)
            val prefix = parts.first()
            val content = parts.getOrNull(1) ?: continue

            if (prefix.length >= bestPrefixLen) continue

            val contentParts = format.end?.split(content, 2)
            val needsFormat = contentParts?.first() ?: content
            val suffix = contentParts?.getOrNull(1)

            if (format.end != null && suffix == null) continue

            best = FormatMatch(Triple(prefix, needsFormat, suffix), format)
            bestPrefixLen = prefix.length
        }

        return best
    }
}
