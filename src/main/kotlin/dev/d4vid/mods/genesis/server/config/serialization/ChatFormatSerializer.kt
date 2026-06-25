package dev.d4vid.mods.genesis.server.config.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.*
import java.net.URI

object ChatFormatSerializer : KSerializer<Style> {
    private val serializer = ListSerializer(String.serializer())

    override val descriptor = SerialDescriptor("dev.d4vid.mods.genesis.ChatFormat", serializer.descriptor)

    override fun serialize(encoder: Encoder, value: Style) {
        val result = buildList {
            value.color?.let { add(it.serialize()) }
            value.hoverEvent?.let { add("hover:content") }
            value.clickEvent?.let { add("click:url") }
            if (value.isBold) add(ChatFormatting.BOLD.name.lowercase())
            if (value.isItalic) add(ChatFormatting.ITALIC.name.lowercase())
            if (value.isUnderlined) add(ChatFormatting.UNDERLINE.name.lowercase())
            if (value.isStrikethrough) add(ChatFormatting.STRIKETHROUGH.name.lowercase())
            if (value.isObfuscated) add(ChatFormatting.OBFUSCATED.name.lowercase())
        }

        encoder.encodeSerializableValue(serializer, result)
    }

    override fun deserialize(decoder: Decoder): Style {
        val values = decoder.decodeSerializableValue(serializer)

        return values.fold(Style.EMPTY) { style, value ->
            ChatFormatting.getByName(value.uppercase())
                ?.let { style.applyFormat(it) }
                ?: when (value) {
                    "hover:content" -> style.withHoverEvent(HoverEvent.ShowText(Component.empty()))
                    "click:url" -> style.withClickEvent(ClickEvent.OpenUrl(URI("https://placeholder.com")))
                    else -> null
                } ?: style.withColor(TextColor.parseColor(value).getOrThrow { SerializationException(it) })
        }
    }
}
