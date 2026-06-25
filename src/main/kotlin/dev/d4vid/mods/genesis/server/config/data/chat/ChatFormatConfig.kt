package dev.d4vid.mods.genesis.server.config.data.chat

import dev.d4vid.mods.genesis.server.config.serialization.ChatFormatSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.network.chat.Style

@Serializable
data class ChatFormatConfig(
    @Serializable(with = ChatFormatSerializer::class)
    val format: Style,
    private val startRegex: String,
    private val endRegex: String? = null,
    val startReplace: String? = null,
    val replaceContent: String? = null,
    val endReplace: String? = null,
    val requireOp: Boolean = false,
) {
    @Transient
    val start = startRegex.toRegex()

    @Transient
    val end = endRegex?.toRegex()
}
