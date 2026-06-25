package dev.d4vid.mods.genesis.server.config.field

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EquipmentSlot

@Serializable(with = ChatKeywordContent.Companion::class)
sealed interface ChatKeywordContent {
    fun content(player: ServerPlayer): Component?

    @Serializable
    enum class Equipment(private val slot: EquipmentSlot) : ChatKeywordContent {
        @SerialName("equipment:hand")
        Hand(EquipmentSlot.MAINHAND),

        @SerialName("equipment:offhand")
        Offhand(EquipmentSlot.MAINHAND),

        @SerialName("equipment:helmet")
        Helmet(EquipmentSlot.HEAD),

        @SerialName("equipment:chestplate")
        Chestplate(EquipmentSlot.CHEST),

        @SerialName("equipment:leggings")
        Leggings(EquipmentSlot.LEGS),

        @SerialName("equipment:boots")
        Boots(EquipmentSlot.FEET);

        override fun content(player: ServerPlayer): Component? {
            val item = player.getItemBySlot(slot)

            if (item.isEmpty) {
                return null
            }

            val component = Component.empty()
                .append("[")
                .append(item.styledHoverName)

            if (item.isStackable) {
                component.append(
                    Component.empty()
                        .append(" x")
                        .append(item.count.toString())
                        .withStyle(ChatFormatting.GRAY)
                )
            }

            return component
                .append("]")
                .withStyle { it.withColor(ChatFormatting.DARK_GRAY).withHoverEvent(HoverEvent.ShowItem(item)) }
        }
    }

    data class Custom(val content: String) : ChatKeywordContent {
        private val component = Component.literal(content)

        override fun content(player: ServerPlayer): Component {
            return component
        }
    }

    companion object : KSerializer<ChatKeywordContent> {
        override val descriptor =
            PrimitiveSerialDescriptor("dev.d4vid.mods.genesis.ChatKeywordContent", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: ChatKeywordContent) {
            when (value) {
                is Equipment -> encoder.encodeSerializableValue(Equipment.serializer(), value)
                is Custom -> encoder.encodeString(value.content)
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun deserialize(decoder: Decoder): ChatKeywordContent {
            val raw = decoder.decodeString()

            return arrayOf(Equipment.serializer()).firstNotNullOfOrNull { serializer ->
                runCatching {
                    serializer.deserialize(object : AbstractDecoder() {
                        override val serializersModule = EmptySerializersModule()
                        override fun decodeString() = raw
                        override fun decodeElementIndex(descriptor: SerialDescriptor) = 0
                        override fun decodeEnum(enumDescriptor: SerialDescriptor) = enumDescriptor.getElementIndex(raw)
                    })
                }.getOrNull()
            } ?: Custom(raw)
        }
    }
}
