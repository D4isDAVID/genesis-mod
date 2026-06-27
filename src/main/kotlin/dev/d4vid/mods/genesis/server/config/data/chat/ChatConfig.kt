package dev.d4vid.mods.genesis.server.config.data.chat

import dev.d4vid.mods.genesis.server.config.field.ChatKeywordContent
import kotlinx.serialization.Serializable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import java.net.URI

@Serializable
data class ChatConfig(
    val formats: List<ChatFormatConfig> = listOf(
        ChatFormatConfig(
            Style.EMPTY.withHoverEvent(HoverEvent.ShowText(Component.empty())),
            """\|\|""",
            """\|\|(?!\|)""",
            replaceContent = "[spoiler]",
        ),
        ChatFormatConfig(
            Style.EMPTY.withClickEvent(ClickEvent.OpenUrl(URI("https://placeholder.com"))),
            """<""",
            """>""",
            requireOp = true,
        ),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.BOLD), """\*\*""", """\*\*(?!\*)"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.ITALIC), """\*""", """\*"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.UNDERLINE), """_""", """_"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.STRIKETHROUGH), """~""", """~"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.GREEN), "^> ", startReplace = "> "),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.OBFUSCATED), """&k"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.BOLD), """&l"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.STRIKETHROUGH), """&m"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.UNDERLINE), """&n"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.ITALIC), """&o"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.RESET), """&r"""),
        ChatFormatConfig(Style.EMPTY.applyFormat(ChatFormatting.GOLD), """&6"""),
    ),
    val keywords: List<ChatKeywordConfig> = listOf(
        ChatKeywordConfig("""[hand]""", ChatKeywordContent.Equipment.Hand),
        ChatKeywordConfig("""[item]""", ChatKeywordContent.Equipment.Hand),
        ChatKeywordConfig("""[i]""", ChatKeywordContent.Equipment.Hand),
        ChatKeywordConfig("""[offhand]""", ChatKeywordContent.Equipment.Offhand),
        ChatKeywordConfig("""[item2]""", ChatKeywordContent.Equipment.Offhand),
        ChatKeywordConfig("""[i2]""", ChatKeywordContent.Equipment.Offhand),
        ChatKeywordConfig("""[helmet]""", ChatKeywordContent.Equipment.Helmet),
        ChatKeywordConfig("""[head]""", ChatKeywordContent.Equipment.Helmet),
        ChatKeywordConfig("""[chestplate]""", ChatKeywordContent.Equipment.Chestplate),
        ChatKeywordConfig("""[chest]""", ChatKeywordContent.Equipment.Chestplate),
        ChatKeywordConfig("""[leggings]""", ChatKeywordContent.Equipment.Leggings),
        ChatKeywordConfig("""[legs]""", ChatKeywordContent.Equipment.Leggings),
        ChatKeywordConfig("""[boots]""", ChatKeywordContent.Equipment.Boots),
        ChatKeywordConfig("""[feet]""", ChatKeywordContent.Equipment.Boots),
        ChatKeywordConfig(""":)""", ChatKeywordContent.Custom("☺")),
        ChatKeywordConfig(""":(""", ChatKeywordContent.Custom("☹")),
        ChatKeywordConfig(""":v:""", ChatKeywordContent.Custom("✌")),
        ChatKeywordConfig(""":check:""", ChatKeywordContent.Custom("✔")),
        ChatKeywordConfig(""":x:""", ChatKeywordContent.Custom("✖")),
        ChatKeywordConfig(""":checkbox:""", ChatKeywordContent.Custom("☑")),
        ChatKeywordConfig(""":xbox:""", ChatKeywordContent.Custom("☒")),
        ChatKeywordConfig(""":box:""", ChatKeywordContent.Custom("☐")),
        ChatKeywordConfig(""":c:""", ChatKeywordContent.Custom("©")),
        ChatKeywordConfig(""":r:""", ChatKeywordContent.Custom("®")),
        ChatKeywordConfig(""":tm:""", ChatKeywordContent.Custom("™")),
        ChatKeywordConfig(""":skull:""", ChatKeywordContent.Custom("☠")),
    ),
)
